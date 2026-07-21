package com.prangyajeet.mvep.order.service;

import com.prangyajeet.mvep.common.enums.NotificationType;
import com.prangyajeet.mvep.common.enums.OrderStatus;
import com.prangyajeet.mvep.notification.service.NotificationService;
import com.prangyajeet.mvep.order.dto.OrderRequestDTO;
import com.prangyajeet.mvep.order.dto.OrderResponseDTO;
import com.prangyajeet.mvep.order.entity.Order;
import com.prangyajeet.mvep.order.repository.OrderRepository;
import com.prangyajeet.mvep.user.entity.User;
import com.prangyajeet.mvep.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        NotificationService notificationService) {

        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
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

        notificationService.sendNotification(
                user,
                "Order Placed Successfully",
                "Your order #" + savedOrder.getId() + " has been placed successfully.",
                NotificationType.ORDER
        );

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

        Order updatedOrder = orderRepository.save(order);

        notificationService.sendNotification(
                updatedOrder.getUser(),
                "Order Confirmed",
                "Your order #" + updatedOrder.getId() + " has been confirmed.",
                NotificationType.ORDER
        );

        return updatedOrder;
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