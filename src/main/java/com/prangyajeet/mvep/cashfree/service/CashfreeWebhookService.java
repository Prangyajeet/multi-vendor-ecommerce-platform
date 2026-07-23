package com.prangyajeet.mvep.cashfree.service;

import com.prangyajeet.mvep.cashfree.dto.CashfreeWebhookDTO;
import com.prangyajeet.mvep.cart.service.CartService;
import com.prangyajeet.mvep.common.enums.PaymentStatus;
import com.prangyajeet.mvep.order.service.OrderService;
import com.prangyajeet.mvep.orderitem.entity.OrderItem;
import com.prangyajeet.mvep.orderitem.service.OrderItemService;
import com.prangyajeet.mvep.payment.entity.Payment;
import com.prangyajeet.mvep.payment.repository.PaymentRepository;
import com.prangyajeet.mvep.product.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CashfreeWebhookService {

    private static final Logger logger =
            LoggerFactory.getLogger(CashfreeWebhookService.class);

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final ProductService productService;
    private final CartService cartService;

    public CashfreeWebhookService(
            PaymentRepository paymentRepository,
            OrderService orderService,
            OrderItemService orderItemService,
            ProductService productService,
            CartService cartService) {

        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.productService = productService;
        this.cartService = cartService;
    }

    public void processWebhook(CashfreeWebhookDTO webhookDTO) {

        logger.info("========== PROCESSING CASHFREE WEBHOOK ==========");

        /*
         * Null Check
         */
        if (webhookDTO == null) {

            logger.warn("Webhook payload is null.");

            return;
        }

        /*
         * Cashfree Dashboard Test Webhook
         */
        if ("WEBHOOK".equalsIgnoreCase(webhookDTO.getType())) {

            logger.info("Cashfree Test Webhook Received Successfully.");

            return;
        }

        /*
         * Validate Payment Webhook
         */
        if (webhookDTO.getData() == null
                || webhookDTO.getData().getOrder() == null
                || webhookDTO.getData().getPayment() == null) {

            logger.warn("Invalid payment webhook payload.");

            return;
        }

        String cashfreeOrderId =
                webhookDTO.getData()
                        .getOrder()
                        .getOrderId();

        Payment payment =
                paymentRepository
                        .findByCfOrderId(cashfreeOrderId)
                        .orElse(null);

        if (payment == null) {

            logger.warn(
                    "Payment not found for Cashfree Order : {}",
                    cashfreeOrderId
            );

            return;
        }

        /*
         * Prevent Duplicate Processing
         */
        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {

            logger.info("Payment already processed.");

            return;
        }

        /*
         * Read Payment Status from Cashfree
         */
        String paymentStatus =
                webhookDTO.getData()
                        .getPayment()
                        .getPaymentStatus();

        if ("SUCCESS".equalsIgnoreCase(paymentStatus)) {

            payment.setPaymentStatus(PaymentStatus.SUCCESS);

            paymentRepository.save(payment);

            logger.info("Payment Updated Successfully.");

            /*
             * Confirm Order
             */
            orderService.confirmOrder(
                    payment.getOrder().getId()
            );

            logger.info("Order Confirmed Successfully.");

            /*
             * Reduce Product Stock
             */
            List<OrderItem> orderItems =
                    orderItemService.getOrderItemEntities(
                            payment.getOrder().getId()
                    );

            for (OrderItem orderItem : orderItems) {

                productService.reduceStock(
                        orderItem.getProduct().getId(),
                        orderItem.getQuantity()
                );
            }

            logger.info("Product Stock Updated Successfully.");

            /*
             * Clear Customer Cart
             */
            cartService.clearCart();

            logger.info("Customer Cart Cleared Successfully.");
        }

        else if ("FAILED".equalsIgnoreCase(paymentStatus)) {

            payment.setPaymentStatus(PaymentStatus.FAILED);

            paymentRepository.save(payment);

            logger.warn("Payment Failed.");
        }

        else if ("PENDING".equalsIgnoreCase(paymentStatus)) {

            payment.setPaymentStatus(PaymentStatus.PENDING);

            paymentRepository.save(payment);

            logger.info("Payment Pending.");
        }

        else if ("CANCELLED".equalsIgnoreCase(paymentStatus)) {

            payment.setPaymentStatus(PaymentStatus.CANCELLED);

            paymentRepository.save(payment);

            logger.warn("Payment Cancelled.");
        }

        else {

            logger.warn(
                    "Unknown Payment Status : {}",
                    paymentStatus
            );
        }

        logger.info("========== CASHFREE WEBHOOK COMPLETED ==========");
    }
}