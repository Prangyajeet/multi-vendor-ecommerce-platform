package com.prangyajeet.mvep.payment.controller;

import com.prangyajeet.mvep.payment.dto.PaymentRequestDTO;
import com.prangyajeet.mvep.payment.dto.PaymentResponseDTO;
import com.prangyajeet.mvep.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.prangyajeet.mvep.response.ApiResponse;
import com.prangyajeet.mvep.payment.dto.PaymentVerificationResponseDTO;

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
    
 // =====================================
 // CUSTOMER APIs
 // =====================================

 @GetMapping("/customer/payments")
 public ResponseEntity<ApiResponse<List<PaymentResponseDTO>>> getMyPayments() {

     return ResponseEntity.ok(
             new ApiResponse<>(
                     true,
                     "Customer payment history fetched successfully.",
                     paymentService.getMyPayments()
             )
     );
 }


 // =====================================
 // ADMIN APIs
 // =====================================

 @GetMapping("/admin/payments")
 public ResponseEntity<ApiResponse<List<PaymentResponseDTO>>> getAllPaymentHistory() {

     return ResponseEntity.ok(
             new ApiResponse<>(
                     true,
                     "All payment history fetched successfully.",
                     paymentService.getAllPaymentHistory()
             )
     );
 }
 
 @GetMapping("/customer/payments/{paymentId}/verify")
 public ResponseEntity<PaymentVerificationResponseDTO> verifyPayment(

         @PathVariable Long paymentId) {

     return ResponseEntity.ok(

             paymentService.verifyPayment(
                     paymentId
             )
     );
 }

}