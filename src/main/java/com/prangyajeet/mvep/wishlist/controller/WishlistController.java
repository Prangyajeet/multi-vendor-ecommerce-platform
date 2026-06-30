package com.prangyajeet.mvep.wishlist.controller;

import com.prangyajeet.mvep.response.ApiResponse;
import com.prangyajeet.mvep.wishlist.dto.WishlistRequestDTO;
import com.prangyajeet.mvep.wishlist.dto.WishlistResponseDTO;
import com.prangyajeet.mvep.wishlist.service.WishlistService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping
    public ApiResponse<WishlistResponseDTO> addToWishlist(
            @Valid @RequestBody WishlistRequestDTO requestDTO) {

        WishlistResponseDTO response =
                wishlistService.addToWishlist(requestDTO);

        return new ApiResponse<>(
                true,
                "Product added to wishlist successfully",
                response
        );
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<WishlistResponseDTO>> getUserWishlist(
            @PathVariable Long userId) {

        List<WishlistResponseDTO> response =
                wishlistService.getUserWishlist(userId);

        return new ApiResponse<>(
                true,
                "Wishlist fetched successfully",
                response
        );
    }

    @DeleteMapping("/{wishlistId}")
    public ApiResponse<String> removeWishlistItem(
            @PathVariable Long wishlistId) {

        wishlistService.removeWishlistItem(wishlistId);

        return new ApiResponse<>(
                true,
                "Wishlist item removed successfully",
                null
        );
    }
}