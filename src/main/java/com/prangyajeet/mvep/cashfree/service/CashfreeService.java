package com.prangyajeet.mvep.cashfree.service;

import com.prangyajeet.mvep.config.CashfreeConfig;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class CashfreeService {

    private final CashfreeConfig cashfreeConfig;
    private final RestTemplate restTemplate;

    public CashfreeService(CashfreeConfig cashfreeConfig) {
        this.cashfreeConfig = cashfreeConfig;
        this.restTemplate = new RestTemplate();
    }

    public Map<String, Object> createOrder(BigDecimal amount,
                                           String customerId,
                                           String customerName,
                                           String customerEmail,
                                           String customerPhone) {

        String url;

        if ("SANDBOX".equalsIgnoreCase(cashfreeConfig.getEnvironment())) {
            url = "https://sandbox.cashfree.com/pg/orders";
        } else {
            url = "https://api.cashfree.com/pg/orders";
        }

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-client-id", cashfreeConfig.getClientId());
        headers.set("x-client-secret", cashfreeConfig.getClientSecret());

        // Use the API version supported by your Cashfree account.
        headers.set("x-api-version", "2023-08-01");

        Map<String, Object> customerDetails = new HashMap<>();
        customerDetails.put("customer_id", customerId);
        customerDetails.put("customer_name", customerName);
        customerDetails.put("customer_email", customerEmail);
        customerDetails.put("customer_phone", customerPhone);

        Map<String, Object> requestBody = new HashMap<>();

        requestBody.put("order_id", "ORDER_" + UUID.randomUUID());
        requestBody.put("order_amount", amount);
        requestBody.put("order_currency", "INR");
        requestBody.put("customer_details", customerDetails);

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(requestBody, headers);

        try {

            ResponseEntity<Map> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            entity,
                            Map.class
                    );

            return response.getBody();

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Cashfree Order Creation Failed : " + ex.getMessage()
            );

        }

    }
    
    public Map<String, Object> createRefund(String cfOrderId,
            BigDecimal refundAmount) {

/*
* Temporary implementation.
* Replace with actual Cashfree Refund API integration later.
*/

Map<String, Object> response = new HashMap<>();

response.put("cf_refund_id", "REFUND_" + UUID.randomUUID());

response.put("refund_status", "SUCCESS");

response.put("cf_order_id", cfOrderId);

response.put("refund_amount", refundAmount);

return response;
}

}