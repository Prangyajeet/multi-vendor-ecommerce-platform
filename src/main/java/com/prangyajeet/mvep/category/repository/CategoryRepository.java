package com.prangyajeet.mvep.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prangyajeet.mvep.category.entity.Category;

import java.util.Optional;

public interface CategoryRepository
        extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);
}