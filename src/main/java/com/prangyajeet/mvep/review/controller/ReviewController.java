package com.prangyajeet.mvep.review.controller;

import com.prangyajeet.mvep.response.ApiResponse;
import com.prangyajeet.mvep.review.dto.ReviewRequestDTO;
import com.prangyajeet.mvep.review.dto.ReviewResponseDTO;
import com.prangyajeet.mvep.review.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ApiResponse<ReviewResponseDTO> addReview(
            @Valid @RequestBody ReviewRequestDTO requestDTO) {

        ReviewResponseDTO response =
                reviewService.addReview(requestDTO);

        return new ApiResponse<>(
                true,
                "Review added successfully",
                response
        );
    }

    @GetMapping("/product/{productId}")
    public ApiResponse<List<ReviewResponseDTO>> getProductReviews(
            @PathVariable Long productId) {

        List<ReviewResponseDTO> response =
                reviewService.getProductReviews(productId);

        return new ApiResponse<>(
                true,
                "Reviews fetched successfully",
                response
        );
    }

    @PutMapping("/{reviewId}")
    public ApiResponse<ReviewResponseDTO> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequestDTO requestDTO) {

        ReviewResponseDTO response =
                reviewService.updateReview(reviewId, requestDTO);

        return new ApiResponse<>(
                true,
                "Review updated successfully",
                response
        );
    }

    @DeleteMapping("/{reviewId}")
    public ApiResponse<String> deleteReview(
            @PathVariable Long reviewId) {

        reviewService.deleteReview(reviewId);

        return new ApiResponse<>(
                true,
                "Review deleted successfully",
                null
        );
    }
}