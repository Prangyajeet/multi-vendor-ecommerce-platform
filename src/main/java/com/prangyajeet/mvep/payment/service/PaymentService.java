package com.prangyajeet.mvep.payment.service;

import com.prangyajeet.mvep.cashfree.service.CashfreeService;
import com.prangyajeet.mvep.common.enums.Currency;
import com.prangyajeet.mvep.common.enums.NotificationType;
import com.prangyajeet.mvep.common.enums.OrderStatus;
import com.prangyajeet.mvep.common.enums.PaymentMethod;
import com.prangyajeet.mvep.common.enums.PaymentStatus;
import com.prangyajeet.mvep.exception.CashfreePaymentException;
import com.prangyajeet.mvep.exception.InvalidPaymentAmountException;
import com.prangyajeet.mvep.exception.OrderNotFoundException;
import com.prangyajeet.mvep.exception.PaymentAlreadyExistsException;
import com.prangyajeet.mvep.exception.PaymentNotFoundException;
import com.prangyajeet.mvep.notification.service.NotificationService;
import com.prangyajeet.mvep.order.entity.Order;
import com.prangyajeet.mvep.order.repository.OrderRepository;
import com.prangyajeet.mvep.payment.dto.PaymentRequestDTO;
import com.prangyajeet.mvep.payment.dto.PaymentResponseDTO;
import com.prangyajeet.mvep.payment.dto.PaymentVerificationResponseDTO;
import com.prangyajeet.mvep.payment.entity.Payment;
import com.prangyajeet.mvep.payment.repository.PaymentRepository;
import com.prangyajeet.mvep.user.entity.User;
import com.prangyajeet.mvep.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final CashfreeService cashfreeService;
    private final UserService userService;
    private final NotificationService notificationService;

    public PaymentService(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository,
            CashfreeService cashfreeService,
            UserService userService,
            NotificationService notificationService) {

        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.cashfreeService = cashfreeService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    public PaymentResponseDTO createPayment(
            PaymentRequestDTO requestDTO) {

        Order order = orderRepository.findById(
                        requestDTO.getOrderId())
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found with id : "
                                        + requestDTO.getOrderId()
                        ));

        paymentRepository.findByOrderId(order.getId())
                .ifPresent(payment -> {
                    throw new PaymentAlreadyExistsException(
                            "Payment already exists for order id : "
                                    + order.getId()
                    );
                });

        if (requestDTO.getAmount()
                .compareTo(order.getTotalAmount()) != 0) {

            throw new InvalidPaymentAmountException(
                    "Payment amount does not match order total."
            );
        }

        if (requestDTO.getPaymentMethod()
                == PaymentMethod.COD) {

            return createCODPayment(order, requestDTO);
        }

        return createCashfreePayment(order, requestDTO);
    }

    public PaymentVerificationResponseDTO verifyPayment(
            Long paymentId) {

        Payment payment =
                paymentRepository.findById(paymentId)
                        .orElseThrow(() ->
                                new PaymentNotFoundException(
                                        "Payment not found with id : "
                                                + paymentId
                                ));

        PaymentVerificationResponseDTO response =
                new PaymentVerificationResponseDTO();

        response.setPaymentId(payment.getId());

        response.setOrderId(
                payment.getOrder().getId()
        );

        response.setPaymentStatus(
                payment.getPaymentStatus()
        );

        response.setOrderStatus(
                payment.getOrder().getOrderStatus()
        );

        response.setMessage(
                "Payment verified successfully."
        );

        return response;
    }

    private PaymentResponseDTO createCODPayment(
            Order order,
            PaymentRequestDTO requestDTO) {

        Payment payment = new Payment();

        payment.setOrder(order);

        payment.setAmount(
                requestDTO.getAmount()
        );

        payment.setCurrency(
                Currency.INR
        );

        payment.setPaymentMethod(
                PaymentMethod.COD
        );

        payment.setPaymentStatus(
                PaymentStatus.PENDING
        );

        payment.setGatewayName(
                "COD"
        );

        payment.setTransactionId(
                UUID.randomUUID().toString()
        );

        payment.setPaymentDate(
                LocalDateTime.now()
        );

        order.setOrderStatus(
                OrderStatus.PLACED
        );

        orderRepository.save(order);

        Payment savedPayment =
                paymentRepository.save(payment);

        notificationService.sendNotification(
                order.getUser(),
                "Payment Pending",
                "Your Cash on Delivery payment for Order #"
                        + order.getId()
                        + " is pending.",
                NotificationType.PAYMENT
        );

        return convertToDTO(savedPayment);
    }
    private PaymentResponseDTO createCashfreePayment(
            Order order,
            PaymentRequestDTO requestDTO) {

        Payment payment = new Payment();

        payment.setOrder(order);

        payment.setAmount(
                requestDTO.getAmount()
        );

        // Production Fix:
        // Currently MVEP supports only INR.
        // Currency is set by the backend instead of trusting client input.
        payment.setCurrency(
                Currency.INR
        );

        payment.setPaymentMethod(
                requestDTO.getPaymentMethod()
        );

        payment.setPaymentStatus(
                PaymentStatus.PENDING
        );

        payment.setGatewayName(
                "CASHFREE"
        );

        payment.setPaymentDate(
                LocalDateTime.now()
        );

        Map<String, Object> cashfreeResponse =
                cashfreeService.createOrder(
                        requestDTO.getAmount(),
                        String.valueOf(order.getUser().getId()),
                        order.getUser().getFirstName(),
                        order.getUser().getEmail(),
                        order.getUser().getPhoneNumber()
                );

        if (cashfreeResponse != null) {

            payment.setGatewayOrderId(
                    (String) cashfreeResponse.get("order_id")
            );

            payment.setCfOrderId(
                    (String) cashfreeResponse.get("cf_order_id")
            );

            payment.setPaymentSessionId(
                    (String) cashfreeResponse.get("payment_session_id")
            );

        } else {

            throw new CashfreePaymentException(
                    "Unable to create Cashfree Order"
            );
        }

        order.setOrderStatus(
                OrderStatus.PAYMENT_PENDING
        );

        orderRepository.save(order);

        Payment savedPayment =
                paymentRepository.save(payment);

        notificationService.sendNotification(
                order.getUser(),
                "Payment Initiated",
                "Your online payment for Order #"
                        + order.getId()
                        + " has been initiated.",
                NotificationType.PAYMENT
        );

        return convertToDTO(savedPayment);
    }

    public PaymentResponseDTO verifyPayment(
            Long paymentId,
            String gatewayPaymentId,
            String gatewaySignature) {

        Payment payment =
                paymentRepository.findById(paymentId)
                        .orElseThrow(() ->
                                new PaymentNotFoundException(
                                        "Payment not found with id : "
                                                + paymentId
                                ));

        payment.setGatewayPaymentId(
                gatewayPaymentId
        );

        payment.setGatewaySignature(
                gatewaySignature
        );

        payment.setPaymentStatus(
                PaymentStatus.SUCCESS
        );

        payment.setTransactionId(
                UUID.randomUUID().toString()
        );

        Order order = payment.getOrder();

        order.setOrderStatus(
                OrderStatus.CONFIRMED
        );

        orderRepository.save(order);

        Payment savedPayment =
                paymentRepository.save(payment);

        notificationService.sendNotification(
                order.getUser(),
                "Payment Successful",
                "Your payment for Order #"
                        + order.getId()
                        + " was successful.",
                NotificationType.PAYMENT
        );

        return convertToDTO(savedPayment);
    }
    public List<PaymentResponseDTO> getAllPayments() {

        return paymentRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public PaymentResponseDTO getPaymentById(
            Long id) {

        Payment payment =
                paymentRepository.findById(id)
                        .orElseThrow(() ->
                                new PaymentNotFoundException(
                                        "Payment not found with id : "
                                                + id
                                ));

        return convertToDTO(payment);
    }

    public PaymentResponseDTO getPaymentByOrderId(
            Long orderId) {

        Payment payment =
                paymentRepository.findByOrderId(orderId)
                        .orElseThrow(() ->
                                new PaymentNotFoundException(
                                        "Payment not found for order id : "
                                                + orderId
                                ));

        return convertToDTO(payment);
    }

    public List<PaymentResponseDTO> getMyPayments() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user =
                userService.getUserByEmail(email);

        return paymentRepository.findByOrderUserId(
                        user.getId())
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<PaymentResponseDTO> getAllPaymentHistory() {

        return paymentRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    private PaymentResponseDTO convertToDTO(
            Payment payment) {

        PaymentResponseDTO dto =
                new PaymentResponseDTO();

        dto.setId(
                payment.getId()
        );

        dto.setOrderId(
                payment.getOrder().getId()
        );

        dto.setAmount(
                payment.getAmount()
        );

        dto.setCurrency(
                payment.getCurrency()
        );

        dto.setPaymentMethod(
                payment.getPaymentMethod()
        );

        dto.setPaymentStatus(
                payment.getPaymentStatus()
        );

        dto.setTransactionId(
                payment.getTransactionId()
        );

        dto.setGatewayName(
                payment.getGatewayName()
        );

        dto.setGatewayOrderId(
                payment.getGatewayOrderId()
        );

        dto.setGatewayPaymentId(
                payment.getGatewayPaymentId()
        );

        dto.setGatewaySignature(
                payment.getGatewaySignature()
        );

        dto.setCfOrderId(
                payment.getCfOrderId()
        );

        dto.setPaymentSessionId(
                payment.getPaymentSessionId()
        );

        dto.setPaymentDate(
                payment.getPaymentDate()
        );

        return dto;
    }
}