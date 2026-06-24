package com.prangyajeet.mvep.cart.service;

import com.prangyajeet.mvep.cart.dto.CartRequestDTO;
import com.prangyajeet.mvep.cart.dto.CartResponseDTO;
import com.prangyajeet.mvep.cart.entity.Cart;
import com.prangyajeet.mvep.cart.repository.CartRepository;
import com.prangyajeet.mvep.product.entity.Product;
import com.prangyajeet.mvep.product.repository.ProductRepository;
import com.prangyajeet.mvep.user.entity.User;
import com.prangyajeet.mvep.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository,
                       UserRepository userRepository,
                       ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public CartResponseDTO addToCart(CartRequestDTO requestDTO) {

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Product product = productRepository.findById(requestDTO.getProductId())
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        Cart cart = new Cart();

        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(requestDTO.getQuantity());

        Cart savedCart = cartRepository.save(cart);

        return mapToResponseDTO(savedCart);
    }

    public List<CartResponseDTO> getUserCart(Long userId) {

        return cartRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public void removeCartItem(Long cartId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() ->
                        new RuntimeException("Cart item not found"));

        cartRepository.delete(cart);
    }

    private CartResponseDTO mapToResponseDTO(Cart cart) {

        CartResponseDTO responseDTO = new CartResponseDTO();

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
}