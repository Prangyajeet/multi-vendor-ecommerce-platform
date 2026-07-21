package com.prangyajeet.mvep.refund.repository;

import com.prangyajeet.mvep.payment.entity.Payment;
import com.prangyajeet.mvep.refund.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefundRepository extends JpaRepository<Refund, Long> {

    Optional<Refund> findByRefundId(String refundId);

    List<Refund> findByPayment(Payment payment);

    List<Refund> findByPaymentId(Long paymentId);

}