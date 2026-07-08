package com.prangyajeet.mvep.cashfree.dto;

public class CashfreeWebhookDTO {

    private String type;

    private CashfreeWebhookDataDTO data;

    public CashfreeWebhookDTO() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CashfreeWebhookDataDTO getData() {
        return data;
    }

    public void setData(CashfreeWebhookDataDTO data) {
        this.data = data;
    }
}