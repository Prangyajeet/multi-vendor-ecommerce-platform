package com.prangyajeet.mvep.cashfree.dto;

public class CashfreeWebhookDataDTO {

    private CashfreeOrderDTO order;

    private CashfreePaymentDTO payment;

    public CashfreeWebhookDataDTO() {
    }

    public CashfreeOrderDTO getOrder() {
        return order;
    }

    public void setOrder(CashfreeOrderDTO order) {
        this.order = order;
    }

    public CashfreePaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(CashfreePaymentDTO payment) {
        this.payment = payment;
    }
}