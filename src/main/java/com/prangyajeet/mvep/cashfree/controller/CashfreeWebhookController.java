package com.prangyajeet.mvep.cashfree.controller;

import com.prangyajeet.mvep.cashfree.dto.CashfreeWebhookDTO;
import com.prangyajeet.mvep.cashfree.service.CashfreeWebhookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cashfree")
public class CashfreeWebhookController {

    private final CashfreeWebhookService cashfreeWebhookService;

    public CashfreeWebhookController(
            CashfreeWebhookService cashfreeWebhookService) {

        this.cashfreeWebhookService = cashfreeWebhookService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(

            @RequestBody CashfreeWebhookDTO webhookDTO,

            @RequestHeader(
                    value = "x-webhook-signature",
                    required = false)
            String signature) {

        cashfreeWebhookService.processWebhook(webhookDTO);

        return ResponseEntity.ok("Webhook Received");
    }
}