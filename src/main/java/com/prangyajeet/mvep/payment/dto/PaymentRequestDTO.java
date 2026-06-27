package com.prangyajeet.mvep.payment.dto;

import com.prangyajeet.mvep.common.enums.Currency;
import com.prangyajeet.mvep.common.enums.PaymentMethod;

import java.math.BigDecimal;

public class PaymentRequestDTO {

    private Long orderId;

    private BigDecimal amount;

    private Currency currency;

    private PaymentMethod paymentMethod;

    public PaymentRequestDTO() {
    }

    public PaymentRequestDTO(Long orderId,
                             BigDecimal amount,
                             Currency currency,
                             PaymentMethod paymentMethod) {

        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}