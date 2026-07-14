package com.prangyajeet.mvep.payment.dto;

import com.prangyajeet.mvep.common.enums.OrderStatus;
import com.prangyajeet.mvep.common.enums.PaymentStatus;

public class PaymentVerificationResponseDTO {

    private Long paymentId;

    private Long orderId;

    private PaymentStatus paymentStatus;

    private OrderStatus orderStatus;

    private String message;

    public PaymentVerificationResponseDTO() {
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}