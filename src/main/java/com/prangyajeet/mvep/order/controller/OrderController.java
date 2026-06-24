package com.prangyajeet.mvep.order.controller;

import com.prangyajeet.mvep.order.dto.OrderRequestDTO;
import com.prangyajeet.mvep.order.dto.OrderResponseDTO;
import com.prangyajeet.mvep.order.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponseDTO placeOrder(
            @RequestBody OrderRequestDTO requestDTO) {

        return orderService.placeOrder(requestDTO);
    }

    @GetMapping("/user/{userId}")
    public List<OrderResponseDTO> getUserOrders(
            @PathVariable Long userId) {

        return orderService.getUserOrders(userId);
    }
}