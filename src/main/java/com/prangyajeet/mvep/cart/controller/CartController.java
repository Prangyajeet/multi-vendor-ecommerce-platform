package com.prangyajeet.mvep.cart.controller;

import com.prangyajeet.mvep.cart.dto.CartRequestDTO;
import com.prangyajeet.mvep.cart.dto.CartResponseDTO;
import com.prangyajeet.mvep.cart.service.CartService;
import com.prangyajeet.mvep.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Add Product To Cart
     */
    @PostMapping
    public ApiResponse<CartResponseDTO> addToCart(
            @Valid @RequestBody CartRequestDTO requestDTO) {

        CartResponseDTO response =
                cartService.addToCart(requestDTO);

        return ApiResponse.success(
                "Product added to cart successfully",
                response
        );
    }

    /**
     * Get Logged-in Customer Cart
     */
    @GetMapping
    public ApiResponse<List<CartResponseDTO>> getMyCart() {

        List<CartResponseDTO> response =
                cartService.getMyCart();

        return ApiResponse.success(
                "Cart fetched successfully",
                response
        );
    }

    /**
     * Update Cart Quantity
     */
    @PutMapping("/{cartId}")
    public ApiResponse<CartResponseDTO> updateCartQuantity(
            @PathVariable Long cartId,
            @RequestBody CartRequestDTO requestDTO) {

        CartResponseDTO response =
                cartService.updateCartQuantity(
                        cartId,
                        requestDTO.getQuantity()
                );

        return ApiResponse.success(
                "Cart updated successfully",
                response
        );
    }

    /**
     * Remove Cart Item
     */
    @DeleteMapping("/{cartId}")
    public ApiResponse<String> removeCartItem(
            @PathVariable Long cartId) {

        cartService.removeCartItem(cartId);

        return ApiResponse.success(
                "Cart item removed successfully",
                null
        );
    }

    /**
     * Clear Logged-in Customer Cart
     */
    @DeleteMapping
    public ApiResponse<String> clearCart() {

        cartService.clearCart();

        return ApiResponse.success(
                "Cart cleared successfully",
                null
        );
    }
}