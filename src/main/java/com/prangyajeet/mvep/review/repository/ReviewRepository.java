package com.prangyajeet.mvep.review.repository;

import com.prangyajeet.mvep.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductId(Long productId);

    List<Review> findByUserId(Long userId);

    Optional<Review> findByUserIdAndProductId(
            Long userId,
            Long productId
    );
}