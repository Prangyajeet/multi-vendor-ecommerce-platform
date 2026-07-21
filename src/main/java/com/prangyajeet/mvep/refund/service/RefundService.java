package com.prangyajeet.mvep.refund.service;

import com.prangyajeet.mvep.cashfree.service.CashfreeService;
import com.prangyajeet.mvep.common.enums.NotificationType;
import com.prangyajeet.mvep.common.enums.PaymentStatus;
import com.prangyajeet.mvep.common.enums.RefundStatus;
import com.prangyajeet.mvep.notification.service.NotificationService;
import com.prangyajeet.mvep.payment.entity.Payment;
import com.prangyajeet.mvep.payment.repository.PaymentRepository;
import com.prangyajeet.mvep.refund.dto.RefundRequestDTO;
import com.prangyajeet.mvep.refund.dto.RefundResponseDTO;
import com.prangyajeet.mvep.refund.entity.Refund;
import com.prangyajeet.mvep.refund.repository.RefundRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class RefundService {

    private final RefundRepository refundRepository;
    private final PaymentRepository paymentRepository;
    private final CashfreeService cashfreeService;
    private final NotificationService notificationService;

    public RefundService(
            RefundRepository refundRepository,
            PaymentRepository paymentRepository,
            CashfreeService cashfreeService,
            NotificationService notificationService) {

        this.refundRepository = refundRepository;
        this.paymentRepository = paymentRepository;
        this.cashfreeService = cashfreeService;
        this.notificationService = notificationService;
    }

    /*
     * Create Refund
     */
    public RefundResponseDTO createRefund(
            RefundRequestDTO requestDTO) {

        Payment payment =
                paymentRepository.findById(
                        requestDTO.getPaymentId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Payment not found."
                        ));

        /*
         * Only successful payments
         * can be refunded
         */
        if (payment.getPaymentStatus()
                != PaymentStatus.SUCCESS) {

            throw new RuntimeException(
                    "Only successful payments can be refunded."
            );
        }

        /*
         * Refund amount validation
         */
        if (requestDTO.getRefundAmount()
                .compareTo(payment.getAmount()) > 0) {

            throw new RuntimeException(
                    "Refund amount exceeds payment amount."
            );
        }

        /*
         * Prevent duplicate refund
         */
        List<Refund> refunds =
                refundRepository.findByPaymentId(
                        payment.getId()
                );

        if (!refunds.isEmpty()) {

            throw new RuntimeException(
                    "Refund already exists for this payment."
            );
        }

        Refund refund = new Refund();

        refund.setPayment(payment);

        refund.setRefundAmount(
                requestDTO.getRefundAmount()
        );

        refund.setRefundStatus(
                RefundStatus.PENDING
        );

        refund.setRefundDate(
                LocalDateTime.now()
        );

        /*
         * Cashfree Refund API
         */
        Map<String, Object> response =
                cashfreeService.createRefund(
                        payment.getCfOrderId(),
                        requestDTO.getRefundAmount()
                );

        if (response != null) {

            Object refundId =
                    response.get("cf_refund_id");

            if (refundId != null) {

                refund.setRefundId(
                        refundId.toString()
                );

            } else {

                refund.setRefundId(
                        UUID.randomUUID().toString()
                );
            }

            refund.setRefundResponse(
                    response.toString()
            );

            refund.setRefundStatus(
                    RefundStatus.SUCCESS
            );

            notificationService.sendNotification(
                    payment.getOrder().getUser(),
                    "Refund Successful",
                    "Your refund for Order #"
                            + payment.getOrder().getId()
                            + " has been processed successfully.",
                    NotificationType.REFUND
            );

        } else {

            refund.setRefundStatus(
                    RefundStatus.FAILED
            );

            notificationService.sendNotification(
                    payment.getOrder().getUser(),
                    "Refund Failed",
                    "Refund processing failed for Order #"
                            + payment.getOrder().getId()
                            + ". Please contact support.",
                    NotificationType.REFUND
            );
        }

        Refund savedRefund =
                refundRepository.save(refund);

        return convertToDTO(savedRefund);
    }    /*
     * Get Refund By Id
     */
    public RefundResponseDTO getRefundById(
            Long refundId) {

        Refund refund =
                refundRepository.findById(refundId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Refund not found."
                                ));

        return convertToDTO(refund);
    }

    /*
     * Get Refunds By Payment
     */
    public List<RefundResponseDTO> getRefundsByPaymentId(
            Long paymentId) {

        return refundRepository.findByPaymentId(paymentId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    /*
     * Get All Refunds
     */
    public List<RefundResponseDTO> getAllRefunds() {

        return refundRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    /*
     * Convert Entity to DTO
     */
    private RefundResponseDTO convertToDTO(
            Refund refund) {

        RefundResponseDTO dto =
                new RefundResponseDTO();

        dto.setId(
                refund.getId()
        );

        dto.setPaymentId(
                refund.getPayment().getId()
        );

        dto.setRefundAmount(
                refund.getRefundAmount()
        );

        dto.setRefundStatus(
                refund.getRefundStatus()
        );

        dto.setRefundId(
                refund.getRefundId()
        );

        dto.setRefundResponse(
                refund.getRefundResponse()
        );

        dto.setRefundDate(
                refund.getRefundDate()
        );

        return dto;
    }

}