package com.prangyajeet.mvep.payment.repository;

import com.prangyajeet.mvep.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import com.prangyajeet.mvep.common.enums.PaymentStatus;
import java.util.List;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(Long orderId);

    Optional<Payment> findByTransactionId(String transactionId);

    Optional<Payment> findByGatewayOrderId(String gatewayOrderId);

    Optional<Payment> findByCfOrderId(String cfOrderId);
    List<Payment> findByOrderUserId(Long userId);

    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);

}