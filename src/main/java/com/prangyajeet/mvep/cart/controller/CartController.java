package com.prangyajeet.mvep.cart.controller;

import com.prangyajeet.mvep.cart.dto.CartRequestDTO;
import com.prangyajeet.mvep.cart.dto.CartResponseDTO;
import com.prangyajeet.mvep.cart.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public CartResponseDTO addToCart(
            @RequestBody CartRequestDTO requestDTO) {

        return cartService.addToCart(requestDTO);
    }

    @GetMapping("/user/{userId}")
    public List<CartResponseDTO> getUserCart(
            @PathVariable Long userId) {

        return cartService.getUserCart(userId);
    }

    @DeleteMapping("/{cartId}")
    public String removeCartItem(
            @PathVariable Long cartId) {

        cartService.removeCartItem(cartId);

        return "Cart item removed successfully";
    }
}