package com.prangyajeet.mvep.vendor.repository;

import com.prangyajeet.mvep.vendor.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

    Optional<Vendor> findByUserId(Long userId);

    Optional<Vendor> findByGstNumber(String gstNumber);

    Optional<Vendor> findByBusinessName(String businessName);

}