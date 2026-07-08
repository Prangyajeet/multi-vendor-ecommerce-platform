package com.prangyajeet.mvep.checkout.dto;

import com.prangyajeet.mvep.common.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public class CheckoutRequestDTO {

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    public CheckoutRequestDTO() {
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}