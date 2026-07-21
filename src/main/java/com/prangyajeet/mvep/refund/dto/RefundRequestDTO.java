package com.prangyajeet.mvep.refund.dto;

import java.math.BigDecimal;

public class RefundRequestDTO {

    private Long paymentId;

    private BigDecimal refundAmount;

    public RefundRequestDTO() {
    }

    public RefundRequestDTO(Long paymentId, BigDecimal refundAmount) {
        this.paymentId = paymentId;
        this.refundAmount = refundAmount;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }
}