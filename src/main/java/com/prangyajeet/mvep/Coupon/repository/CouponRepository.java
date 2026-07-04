package com.prangyajeet.mvep.Coupon.repository;

import com.prangyajeet.mvep.Coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    boolean existsByCode(String code);

    Optional<Coupon> findByCode(String code);

    List<Coupon> findByActiveTrue();

    List<Coupon> findByActiveFalse();

    List<Coupon> findByCodeContainingIgnoreCase(String keyword);
}