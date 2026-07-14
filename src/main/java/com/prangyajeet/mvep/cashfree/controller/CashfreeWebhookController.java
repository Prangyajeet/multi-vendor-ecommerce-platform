package com.prangyajeet.mvep.cashfree.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prangyajeet.mvep.cashfree.dto.CashfreeWebhookDTO;
import com.prangyajeet.mvep.cashfree.service.CashfreeWebhookService;
import com.prangyajeet.mvep.cashfree.service.WebhookSignatureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cashfree")
public class CashfreeWebhookController {

    private final CashfreeWebhookService cashfreeWebhookService;
    private final WebhookSignatureService webhookSignatureService;
    private final ObjectMapper objectMapper;

    public CashfreeWebhookController(
            CashfreeWebhookService cashfreeWebhookService,
            WebhookSignatureService webhookSignatureService,
            ObjectMapper objectMapper) {

        this.cashfreeWebhookService = cashfreeWebhookService;
        this.webhookSignatureService = webhookSignatureService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(

            @RequestBody String rawBody,

            @RequestHeader(value = "x-webhook-signature", required = false)
            String signature,

            @RequestHeader(value = "x-webhook-timestamp", required = false)
            String timestamp) {

        System.out.println("========== WEBHOOK CONTROLLER HIT ==========");
        System.out.println("Raw Body : " + rawBody);
        System.out.println("Timestamp : " + timestamp);
        System.out.println("Signature : " + signature);

        if (signature == null || timestamp == null) {

            System.out.println("Missing Headers");

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Missing Signature");
        }

        boolean verified =
                webhookSignatureService.verifySignature(
                        timestamp,
                        rawBody,
                        signature
                );

        System.out.println("Verified : " + verified);

        if (!verified) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid Signature");
        }

        try {

            CashfreeWebhookDTO webhookDTO =
                    objectMapper.readValue(
                            rawBody,
                            CashfreeWebhookDTO.class
                    );

            cashfreeWebhookService.processWebhook(
                    webhookDTO
            );

            return ResponseEntity.ok("Webhook Received");

        } catch (Exception ex) {

            ex.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid Payload");
        }
    }
}