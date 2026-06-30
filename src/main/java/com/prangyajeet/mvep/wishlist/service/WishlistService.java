package com.prangyajeet.mvep.wishlist.service;

import com.prangyajeet.mvep.exception.ProductNotFoundException;
import com.prangyajeet.mvep.exception.WishlistNotFoundException;
import com.prangyajeet.mvep.product.entity.Product;
import com.prangyajeet.mvep.product.repository.ProductRepository;
import com.prangyajeet.mvep.user.entity.User;
import com.prangyajeet.mvep.user.repository.UserRepository;
import com.prangyajeet.mvep.wishlist.dto.WishlistRequestDTO;
import com.prangyajeet.mvep.wishlist.dto.WishlistResponseDTO;
import com.prangyajeet.mvep.wishlist.entity.Wishlist;
import com.prangyajeet.mvep.wishlist.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public WishlistService(
            WishlistRepository wishlistRepository,
            UserRepository userRepository,
            ProductRepository productRepository) {

        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public WishlistResponseDTO addToWishlist(
            WishlistRequestDTO requestDTO) {

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Product product = productRepository.findById(requestDTO.getProductId())
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id : "
                                        + requestDTO.getProductId()));

        Wishlist existingWishlist =
                wishlistRepository.findByUserIdAndProductId(
                        requestDTO.getUserId(),
                        requestDTO.getProductId())
                        .orElse(null);

        if (existingWishlist != null) {

            throw new RuntimeException(
                    "Product already exists in wishlist."
            );
        }

        Wishlist wishlist = new Wishlist();

        wishlist.setUser(user);
        wishlist.setProduct(product);

        Wishlist savedWishlist =
                wishlistRepository.save(wishlist);

        return mapToResponseDTO(savedWishlist);
    }

    public List<WishlistResponseDTO> getUserWishlist(
            Long userId) {

        return wishlistRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public void removeWishlistItem(
            Long wishlistId) {

        Wishlist wishlist =
                wishlistRepository.findById(wishlistId)
                        .orElseThrow(() ->
                                new WishlistNotFoundException(
                                        "Wishlist item not found with id : "
                                                + wishlistId));

        wishlistRepository.delete(wishlist);
    }

    private WishlistResponseDTO mapToResponseDTO(
            Wishlist wishlist) {

        WishlistResponseDTO response =
                new WishlistResponseDTO();

        response.setId(
                wishlist.getId()
        );

        response.setUserId(
                wishlist.getUser().getId()
        );

        response.setProductId(
                wishlist.getProduct().getId()
        );

        response.setProductName(
                wishlist.getProduct().getName()
        );

        response.setImageUrl(
                wishlist.getProduct().getImageUrl()
        );

        response.setProductPrice(
                wishlist.getProduct().getPrice()
        );

        return response;
    }
}