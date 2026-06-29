package com.prangyajeet.mvep.product.repository;

import com.prangyajeet.mvep.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryId(Long categoryId);

    @Query("SELECT p FROM Product p WHERE p.vendor.id = :vendorId")
    List<Product> findProductsByVendorId(@Param("vendorId") Long vendorId);

}