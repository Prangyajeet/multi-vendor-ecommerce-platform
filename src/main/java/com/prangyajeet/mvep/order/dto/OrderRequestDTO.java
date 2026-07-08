package com.prangyajeet.mvep.order.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class OrderRequestDTO {

    @NotNull(message = "User Id is required")
    private Long userId;

    @NotNull(message = "Total amount is required")
    private BigDecimal totalAmount;

    public OrderRequestDTO() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}