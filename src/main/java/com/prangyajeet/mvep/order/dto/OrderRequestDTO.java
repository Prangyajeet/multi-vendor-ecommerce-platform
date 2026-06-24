package com.prangyajeet.mvep.order.dto;

import java.math.BigDecimal;

public class OrderRequestDTO {

    private Long userId;
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