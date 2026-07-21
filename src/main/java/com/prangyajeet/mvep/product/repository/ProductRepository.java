package com.prangyajeet.mvep.product.repository;

import com.prangyajeet.mvep.product.entity.Product;
import com.prangyajeet.mvep.common.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryId(Long categoryId);

    @Query("SELECT p FROM Product p WHERE p.vendor.id = :vendorId")
    List<Product> findProductsByVendorId(@Param("vendorId") Long vendorId);

    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findByPriceBetween(
            BigDecimal minPrice,
            BigDecimal maxPrice
    );

    List<Product> findByStatus(ProductStatus status);

    List<Product> findByCategoryIdAndStatus(
            Long categoryId,
            ProductStatus status
    );

    List<Product> findByVendorIdAndStatus(
            Long vendorId,
            ProductStatus status
    );

    List<Product> findByNameContainingIgnoreCaseAndStatus(
            String keyword,
            ProductStatus status
    );
}