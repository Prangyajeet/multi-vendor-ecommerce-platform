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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CashfreeWebhookService {

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

        System.out.println("========== PROCESSING CASHFREE WEBHOOK ==========");

        if (webhookDTO == null
                || webhookDTO.getData() == null
                || webhookDTO.getData().getOrder() == null) {

            System.out.println("Invalid webhook payload.");
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

            System.out.println(
                    "Payment not found for Cashfree Order : "
                            + cashfreeOrderId
            );

            return;
        }

        /*
         * Prevent duplicate webhook processing
         */
        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {

            System.out.println("Payment already processed.");

            return;
        }

        /*
         * Update Payment Status
         */
        payment.setPaymentStatus(PaymentStatus.SUCCESS);

        paymentRepository.save(payment);

        System.out.println("Payment Updated Successfully.");

        /*
         * Confirm Order
         */
        orderService.confirmOrder(
                payment.getOrder().getId()
        );

        System.out.println("Order Confirmed Successfully.");

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

        System.out.println("Product Stock Updated Successfully.");

        /*
         * Clear Customer Cart
         */
        cartService.clearCart(
                payment.getOrder()
                        .getUser()
                        .getId()
        );

        System.out.println("Customer Cart Cleared Successfully.");

        System.out.println("===============================================");
    }
}