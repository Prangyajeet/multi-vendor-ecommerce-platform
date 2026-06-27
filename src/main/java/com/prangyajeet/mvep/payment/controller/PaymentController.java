package com.prangyajeet.mvep.payment.controller;

import com.prangyajeet.mvep.payment.dto.PaymentRequestDTO;
import com.prangyajeet.mvep.payment.dto.PaymentResponseDTO;
import com.prangyajeet.mvep.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(
            @RequestBody PaymentRequestDTO requestDTO) {

        PaymentResponseDTO response =
                paymentService.createPayment(requestDTO);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{paymentId}/verify")
    public ResponseEntity<PaymentResponseDTO> verifyPayment(

            @PathVariable Long paymentId,

            @RequestParam String gatewayPaymentId,

            @RequestParam String gatewaySignature) {

        PaymentResponseDTO response =
                paymentService.verifyPayment(
                        paymentId,
                        gatewayPaymentId,
                        gatewaySignature
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {

        return ResponseEntity.ok(
                paymentService.getAllPayments()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                paymentService.getPaymentById(id)
        );
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByOrderId(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                paymentService.getPaymentByOrderId(orderId)
        );
    }

}