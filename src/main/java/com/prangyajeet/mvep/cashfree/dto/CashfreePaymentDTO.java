package com.prangyajeet.mvep.cashfree.dto;

import java.math.BigDecimal;

public class CashfreePaymentDTO {

    private String cfPaymentId;

    private String paymentStatus;

    private String paymentMethod;

    private BigDecimal paymentAmount;

    public CashfreePaymentDTO() {
    }

    public String getCfPaymentId() {
        return cfPaymentId;
    }

    public void setCfPaymentId(String cfPaymentId) {
        this.cfPaymentId = cfPaymentId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
}