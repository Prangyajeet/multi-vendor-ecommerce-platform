package com.prangyajeet.mvep.checkout.service;

import com.prangyajeet.mvep.cart.dto.CartResponseDTO;
import com.prangyajeet.mvep.cart.service.CartService;
import com.prangyajeet.mvep.checkout.dto.CheckoutRequestDTO;
import com.prangyajeet.mvep.checkout.dto.CheckoutResponseDTO;
import com.prangyajeet.mvep.exception.CartNotFoundException;
import com.prangyajeet.mvep.exception.InsufficientStockException;
import com.prangyajeet.mvep.user.entity.User;
import com.prangyajeet.mvep.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.prangyajeet.mvep.order.dto.OrderRequestDTO;
import com.prangyajeet.mvep.order.dto.OrderResponseDTO;
import com.prangyajeet.mvep.order.service.OrderService;
import com.prangyajeet.mvep.orderitem.dto.OrderItemRequestDTO;
import com.prangyajeet.mvep.orderitem.service.OrderItemService;
import com.prangyajeet.mvep.common.enums.Currency;
import com.prangyajeet.mvep.payment.dto.PaymentRequestDTO;
import com.prangyajeet.mvep.payment.dto.PaymentResponseDTO;
import com.prangyajeet.mvep.payment.service.PaymentService;
import org.springframework.transaction.annotation.Transactional;
import com.prangyajeet.mvep.product.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CheckoutService {

	private final UserService userService;
	private final CartService cartService;
	private final OrderService orderService;
	private final OrderItemService orderItemService;
	private final PaymentService paymentService;
	private final ProductService productService;

	public CheckoutService(UserService userService,
            CartService cartService,
            OrderService orderService,
            OrderItemService orderItemService,
            PaymentService paymentService,
            ProductService productService) {

this.userService = userService;
this.cartService = cartService;
this.orderService = orderService;
this.orderItemService = orderItemService;
this.paymentService = paymentService;
this.productService = productService;
}

	@Transactional
	public CheckoutResponseDTO checkout(CheckoutRequestDTO requestDTO) {

        // Get logged-in user
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userService.getUserByEmail(email);

        // Load cart
        List<CartResponseDTO> cartItems =
        		cartService.getMyCart();

        // Check if cart is empty
        if (cartItems.isEmpty()) {
            throw new CartNotFoundException("Your cart is empty.");
        }

        // Validate stock
        for (CartResponseDTO item : cartItems) {

            if (item.getQuantity() > item.getAvailableStock()) {

                throw new InsufficientStockException(
                        "Only "
                                + item.getAvailableStock()
                                + " unit(s) available for "
                                + item.getProductName()
                );
            }
        }

        // Calculate subtotal
        BigDecimal subtotal = BigDecimal.ZERO;

        for (CartResponseDTO item : cartItems) {

            subtotal = subtotal.add(
                    item.getTotalPrice()
            );
        }
        
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();

        orderRequestDTO.setUserId(user.getId());
        orderRequestDTO.setTotalAmount(subtotal);

        OrderResponseDTO orderResponseDTO =
                orderService.placeOrder(orderRequestDTO);
        
        for (CartResponseDTO item : cartItems) {

            OrderItemRequestDTO orderItemRequestDTO =
                    new OrderItemRequestDTO();

            orderItemRequestDTO.setOrderId(
                    orderResponseDTO.getId()
            );

            orderItemRequestDTO.setProductId(
                    item.getProductId()
            );

            orderItemRequestDTO.setQuantity(
                    item.getQuantity()
            );

            orderItemService.createOrderItem(
                    orderItemRequestDTO
            );

            // Reduce stock after creating order item
            productService.reduceStock(
                    item.getProductId(),
                    item.getQuantity()
            );
        }
        
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();

        paymentRequestDTO.setOrderId(orderResponseDTO.getId());
        paymentRequestDTO.setAmount(subtotal);
        paymentRequestDTO.setCurrency(Currency.INR);
        paymentRequestDTO.setPaymentMethod(requestDTO.getPaymentMethod());

        PaymentResponseDTO paymentResponseDTO =
                paymentService.createPayment(paymentRequestDTO);

        // Prepare response
        CheckoutResponseDTO responseDTO =
                new CheckoutResponseDTO();
        
        responseDTO.setOrderId(
                orderResponseDTO.getId()
        );

        responseDTO.setTotalAmount(
                orderResponseDTO.getTotalAmount()
        );

        responseDTO.setOrderStatus(
                orderResponseDTO.getOrderStatus()
        );

        responseDTO.setPaymentMethod(
                paymentResponseDTO.getPaymentMethod()
        );

        responseDTO.setPaymentStatus(
                paymentResponseDTO.getPaymentStatus()
        );

        responseDTO.setPaymentSessionId(
                paymentResponseDTO.getPaymentSessionId()
        );

        responseDTO.setMessage(
                "Checkout completed successfully."
        );
        
        cartService.clearCart();
        
        return responseDTO;
    }
}