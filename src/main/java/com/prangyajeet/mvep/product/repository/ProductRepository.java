package com.prangyajeet.mvep.product.repository;

import com.prangyajeet.mvep.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}