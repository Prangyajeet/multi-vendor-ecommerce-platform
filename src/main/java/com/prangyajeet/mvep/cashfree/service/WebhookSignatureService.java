package com.prangyajeet.mvep.cashfree.service;

import com.prangyajeet.mvep.config.CashfreeConfig;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class WebhookSignatureService {

    private static final String HMAC_SHA256 = "HmacSHA256";

    private final CashfreeConfig cashfreeConfig;

    public WebhookSignatureService(
            CashfreeConfig cashfreeConfig) {

        this.cashfreeConfig = cashfreeConfig;
    }

    public boolean verifySignature(
            String timestamp,
            String rawBody,
            String receivedSignature) {

        try {

            String payload = timestamp + rawBody;

            Mac mac = Mac.getInstance(HMAC_SHA256);

            SecretKeySpec secretKeySpec =
                    new SecretKeySpec(
                            cashfreeConfig.getClientSecret()
                                    .getBytes(StandardCharsets.UTF_8),
                            HMAC_SHA256
                    );

            mac.init(secretKeySpec);

            byte[] hash = mac.doFinal(
                    payload.getBytes(StandardCharsets.UTF_8)
            );

            String generatedSignature =
                    Base64.getEncoder()
                            .encodeToString(hash);

            System.out.println("========== WEBHOOK SIGNATURE DEBUG ==========");
            System.out.println("Timestamp          : " + timestamp);
            System.out.println("Received Signature : " + receivedSignature);
            System.out.println("Generated Signature: " + generatedSignature);
            System.out.println("Payload            : " + payload);
            System.out.println("=============================================");

            return generatedSignature.equals(receivedSignature);

        } catch (Exception ex) {

            ex.printStackTrace();

            return false;
        }
    }
    
}