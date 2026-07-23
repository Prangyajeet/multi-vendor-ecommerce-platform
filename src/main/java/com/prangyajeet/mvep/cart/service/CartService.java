package com.prangyajeet.mvep.cart.service;

import com.prangyajeet.mvep.cart.dto.CartRequestDTO;
import com.prangyajeet.mvep.cart.dto.CartResponseDTO;
import com.prangyajeet.mvep.cart.entity.Cart;
import com.prangyajeet.mvep.cart.repository.CartRepository;
import com.prangyajeet.mvep.exception.CartNotFoundException;
import com.prangyajeet.mvep.exception.InsufficientStockException;
import com.prangyajeet.mvep.exception.ProductNotFoundException;
import com.prangyajeet.mvep.product.entity.Product;
import com.prangyajeet.mvep.product.repository.ProductRepository;
import com.prangyajeet.mvep.user.entity.User;
import com.prangyajeet.mvep.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(
            CartRepository cartRepository,
            UserRepository userRepository,
            ProductRepository productRepository) {

        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    /**
     * Get Logged-in Customer
     */
    private User getLoggedInUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Logged-in user not found."
                        ));
    }

    /**
     * Add Product To Cart
     */
    public CartResponseDTO addToCart(
            CartRequestDTO requestDTO) {

        User user = getLoggedInUser();

        Product product = productRepository.findById(
                requestDTO.getProductId())
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id : "
                                        + requestDTO.getProductId()
                        ));

        if (requestDTO.getQuantity() > product.getStockQuantity()) {

            throw new InsufficientStockException(
                    "Only "
                            + product.getStockQuantity()
                            + " item(s) available in stock."
            );
        }

        Cart cart = cartRepository
                .findByUserIdAndProductId(
                        user.getId(),
                        requestDTO.getProductId()
                )
                .orElse(null);

        if (cart != null) {

            int updatedQuantity =
                    cart.getQuantity()
                            + requestDTO.getQuantity();

            if (updatedQuantity > product.getStockQuantity()) {

                throw new InsufficientStockException(
                        "Only "
                                + product.getStockQuantity()
                                + " item(s) available in stock."
                );
            }

            cart.setQuantity(updatedQuantity);

        } else {

            cart = new Cart();

            cart.setUser(user);
            cart.setProduct(product);
            cart.setQuantity(requestDTO.getQuantity());
        }

        Cart savedCart =
                cartRepository.save(cart);

        return mapToResponseDTO(savedCart);
    }

    /**
     * Get Logged-in Customer Cart
     */
    public List<CartResponseDTO> getMyCart() {

        User user = getLoggedInUser();

        return cartRepository.findByUser(user)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Remove Cart Item
     */
    public void removeCartItem(Long cartId) {

        User user = getLoggedInUser();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() ->
                        new CartNotFoundException(
                                "Cart item not found with id : "
                                        + cartId
                        ));

        if (!cart.getUser().getId().equals(user.getId())) {

            throw new RuntimeException(
                    "You are not authorized to remove this cart item."
            );
        }

        cartRepository.delete(cart);
    }

    /**
     * Clear Logged-in Customer Cart
     */
    @Transactional
    public void clearCart() {

        User user = getLoggedInUser();

        List<Cart> cartItems =
                cartRepository.findByUser(user);

        if (cartItems.isEmpty()) {

            throw new CartNotFoundException(
                    "Cart is already empty."
            );
        }

        cartRepository.deleteAllByUser(user);
    }

    /**
     * Convert Entity To DTO
     */
    private CartResponseDTO mapToResponseDTO(
            Cart cart) {

        CartResponseDTO responseDTO =
                new CartResponseDTO();

        responseDTO.setId(cart.getId());

        responseDTO.setUserId(
                cart.getUser().getId()
        );

        responseDTO.setProductId(
                cart.getProduct().getId()
        );

        responseDTO.setProductName(
                cart.getProduct().getName()
        );

        responseDTO.setProductPrice(
                cart.getProduct().getPrice()
        );

        responseDTO.setAvailableStock(
                cart.getProduct().getStockQuantity()
        );

        responseDTO.setQuantity(
                cart.getQuantity()
        );

        BigDecimal totalPrice =
                cart.getProduct()
                        .getPrice()
                        .multiply(
                                BigDecimal.valueOf(
                                        cart.getQuantity()
                                )
                        );

        responseDTO.setTotalPrice(totalPrice);

        return responseDTO;
    }
    /**
     * Update Cart Quantity
     */
    public CartResponseDTO updateCartQuantity(
            Long cartId,
            Integer quantity) {

        User user = getLoggedInUser();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() ->
                        new CartNotFoundException(
                                "Cart item not found with id : "
                                        + cartId
                        ));

        if (!cart.getUser().getId().equals(user.getId())) {

            throw new RuntimeException(
                    "You are not authorized to update this cart item."
            );
        }

        Product product = cart.getProduct();

        if (quantity > product.getStockQuantity()) {

            throw new InsufficientStockException(
                    "Only "
                            + product.getStockQuantity()
                            + " item(s) available in stock."
            );
        }

        cart.setQuantity(quantity);

        Cart updatedCart =
                cartRepository.save(cart);

        return mapToResponseDTO(updatedCart);
    }
}