package com.prangyajeet.mvep.checkout.dto;

import com.prangyajeet.mvep.common.enums.OrderStatus;
import com.prangyajeet.mvep.common.enums.PaymentMethod;
import com.prangyajeet.mvep.common.enums.PaymentStatus;

import java.math.BigDecimal;

public class CheckoutResponseDTO {

    private Long orderId;

    private BigDecimal totalAmount;

    private OrderStatus orderStatus;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private String paymentSessionId;

    private String message;

    public CheckoutResponseDTO() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentSessionId() {
        return paymentSessionId;
    }

    public void setPaymentSessionId(String paymentSessionId) {
        this.paymentSessionId = paymentSessionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}