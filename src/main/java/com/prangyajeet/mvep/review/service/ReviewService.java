package com.prangyajeet.mvep.review.service;

import com.prangyajeet.mvep.exception.ProductNotFoundException;
import com.prangyajeet.mvep.exception.ReviewAlreadyExistsException;
import com.prangyajeet.mvep.exception.ReviewNotFoundException;
import com.prangyajeet.mvep.order.entity.Order;
import com.prangyajeet.mvep.order.repository.OrderRepository;
import com.prangyajeet.mvep.orderitem.entity.OrderItem;
import com.prangyajeet.mvep.orderitem.repository.OrderItemRepository;
import com.prangyajeet.mvep.product.entity.Product;
import com.prangyajeet.mvep.product.repository.ProductRepository;
import com.prangyajeet.mvep.review.dto.ReviewRequestDTO;
import com.prangyajeet.mvep.review.dto.ReviewResponseDTO;
import com.prangyajeet.mvep.review.entity.Review;
import com.prangyajeet.mvep.review.repository.ReviewRepository;
import com.prangyajeet.mvep.user.entity.User;
import com.prangyajeet.mvep.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.prangyajeet.mvep.exception.ProductNotPurchasedException;
import com.prangyajeet.mvep.exception.UnauthorizedReviewException;
import com.prangyajeet.mvep.exception.UserNotFoundException;
import com.prangyajeet.mvep.common.enums.OrderStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            UserRepository userRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository) {

        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public ReviewResponseDTO addReview(
            ReviewRequestDTO requestDTO) {

        User user = userRepository.findById(requestDTO.getUserId())
        		.orElseThrow(() ->
                new UserNotFoundException(
                        "User not found with id : "
                                + requestDTO.getUserId()
                ));

        Product product = productRepository.findById(requestDTO.getProductId())
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id : "
                                        + requestDTO.getProductId()
                        ));

        // Only CUSTOMER can review
        if (!user.getRole().getName().name().equals("CUSTOMER")) {

            throw new UnauthorizedReviewException(
                    "Only customers can review products."
            );
        }

        // Customer must have purchased this product

        List<Order> orders =
                orderRepository.findByUserId(user.getId());

        boolean hasPurchased = false;

        for (Order order : orders) {

            List<OrderItem> orderItems =
                    orderItemRepository.findByOrderId(
                            order.getId()
                    );

            for (OrderItem item : orderItems) {

                if (item.getProduct().getId()
                        .equals(product.getId())) {

                    hasPurchased = true;
                    break;
                }
            }

            if (hasPurchased) {
                break;
            }
        }

        if (!hasPurchased) {

            throw new ProductNotPurchasedException(
                    "You can review only purchased products."
            );
        }

        Review existingReview =
                reviewRepository.findByUserIdAndProductId(
                        user.getId(),
                        product.getId()
                ).orElse(null);

        if (existingReview != null) {

            throw new ReviewAlreadyExistsException(
                    "You have already reviewed this product."
            );
        }

        Review review = new Review();

        review.setUser(user);
        review.setProduct(product);
        review.setRating(requestDTO.getRating());
        review.setReview(requestDTO.getReview());

        Review savedReview =
                reviewRepository.save(review);

        return mapToResponseDTO(savedReview);
    }    public List<ReviewResponseDTO> getProductReviews(
            Long productId) {

        return reviewRepository.findByProductId(productId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public ReviewResponseDTO updateReview(
            Long reviewId,
            ReviewRequestDTO requestDTO) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new ReviewNotFoundException(
                                "Review not found with id : " + reviewId
                        ));

        review.setRating(requestDTO.getRating());
        review.setReview(requestDTO.getReview());

        Review updatedReview =
                reviewRepository.save(review);

        return mapToResponseDTO(updatedReview);
    }

    public void deleteReview(
            Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new ReviewNotFoundException(
                                "Review not found with id : " + reviewId
                        ));

        reviewRepository.delete(review);
    }

    private ReviewResponseDTO mapToResponseDTO(
            Review review) {

        ReviewResponseDTO response =
                new ReviewResponseDTO();

        response.setId(
                review.getId()
        );

        response.setUserId(
                review.getUser().getId()
        );

        response.setUserName(
                review.getUser().getFirstName()
                        + " "
                        + review.getUser().getLastName()
        );

        response.setProductId(
                review.getProduct().getId()
        );

        response.setProductName(
                review.getProduct().getName()
        );

        response.setRating(
                review.getRating()
        );

        response.setReview(
                review.getReview()
        );

        return response;
    }
}