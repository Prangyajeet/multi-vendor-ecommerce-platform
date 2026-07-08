package com.prangyajeet.mvep.order.service;

import com.prangyajeet.mvep.order.dto.OrderRequestDTO;
import com.prangyajeet.mvep.order.dto.OrderResponseDTO;
import com.prangyajeet.mvep.order.entity.Order;
import com.prangyajeet.mvep.order.repository.OrderRepository;
import com.prangyajeet.mvep.user.entity.User;
import com.prangyajeet.mvep.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.prangyajeet.mvep.common.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public OrderResponseDTO placeOrder(OrderRequestDTO requestDTO) {

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Order order = new Order();

        order.setUser(user);
        order.setTotalAmount(requestDTO.getTotalAmount());
        order.setOrderStatus(OrderStatus.PLACED);
        order.setOrderDate(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        return mapToResponseDTO(savedOrder);
    }

    public List<OrderResponseDTO> getUserOrders(Long userId) {

        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    public Order confirmOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found with id : " + orderId
                        ));

        order.setOrderStatus(OrderStatus.CONFIRMED);

        System.out.println("Order " + orderId + " changed to CONFIRMED");

        return orderRepository.save(order);
    }

    private OrderResponseDTO mapToResponseDTO(Order order) {

        OrderResponseDTO responseDTO = new OrderResponseDTO();

        responseDTO.setId(order.getId());
        responseDTO.setUserId(order.getUser().getId());
        responseDTO.setTotalAmount(order.getTotalAmount());
        responseDTO.setOrderStatus(order.getOrderStatus());
        responseDTO.setOrderDate(order.getOrderDate());

        return responseDTO;
    }
}