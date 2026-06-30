package com.prangyajeet.mvep.cart.controller;

import com.prangyajeet.mvep.cart.dto.CartRequestDTO;
import com.prangyajeet.mvep.cart.dto.CartResponseDTO;
import com.prangyajeet.mvep.cart.service.CartService;
import org.springframework.web.bind.annotation.*;
import com.prangyajeet.mvep.response.ApiResponse;

import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

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
    
    @GetMapping("/user/{userId}")
    public ApiResponse<List<CartResponseDTO>> getUserCart(
            @PathVariable Long userId) {

        List<CartResponseDTO> response =
                cartService.getUserCart(userId);

        return ApiResponse.success(
                "Cart fetched successfully",
                response
        );
    }

    @DeleteMapping("/{cartId}")
    public ApiResponse<String> removeCartItem(
            @PathVariable Long cartId) {

        cartService.removeCartItem(cartId);

        return ApiResponse.success(
                "Cart item removed successfully",
                null
        );
    }
    
    @DeleteMapping("/user/{userId}")
    public ApiResponse<String> clearCart(
            @PathVariable Long userId) {

        cartService.clearCart(userId);

        return ApiResponse.success(
                "Cart cleared successfully",
                null
        );
    }
    
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
    }
