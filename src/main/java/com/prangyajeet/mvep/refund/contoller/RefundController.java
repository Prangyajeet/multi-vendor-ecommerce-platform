package com.prangyajeet.mvep.refund.contoller;

import com.prangyajeet.mvep.refund.dto.RefundRequestDTO;
import com.prangyajeet.mvep.refund.dto.RefundResponseDTO;
import com.prangyajeet.mvep.refund.service.RefundService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refunds")
@CrossOrigin(origins = "*")
public class RefundController {

    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    /*
     * Create Refund
     */
    @PostMapping
    public ResponseEntity<RefundResponseDTO> createRefund(
            @RequestBody RefundRequestDTO requestDTO) {

        RefundResponseDTO response =
                refundService.createRefund(requestDTO);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    /*
     * Get Refund By Id
     */
    @GetMapping("/{refundId}")
    public ResponseEntity<RefundResponseDTO> getRefundById(
            @PathVariable Long refundId) {

        return ResponseEntity.ok(
                refundService.getRefundById(refundId)
        );
    }

    /*
     * Get Refunds By Payment Id
     */
    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<List<RefundResponseDTO>> getRefundsByPaymentId(
            @PathVariable Long paymentId) {

        return ResponseEntity.ok(
                refundService.getRefundsByPaymentId(paymentId)
        );
    }

    /*
     * Get All Refunds
     */
    @GetMapping
    public ResponseEntity<List<RefundResponseDTO>> getAllRefunds() {

        return ResponseEntity.ok(
                refundService.getAllRefunds()
        );
    }

}