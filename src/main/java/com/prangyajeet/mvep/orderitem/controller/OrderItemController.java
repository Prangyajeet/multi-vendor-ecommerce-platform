package com.prangyajeet.mvep.orderitem.controller;

import com.prangyajeet.mvep.orderitem.dto.OrderItemRequestDTO;
import com.prangyajeet.mvep.orderitem.dto.OrderItemResponseDTO;
import com.prangyajeet.mvep.orderitem.service.OrderItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(
            OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @PostMapping
    public OrderItemResponseDTO createOrderItem(
            @RequestBody OrderItemRequestDTO requestDTO) {

        return orderItemService.createOrderItem(
                requestDTO);
    }

    @GetMapping("/order/{orderId}")
    public List<OrderItemResponseDTO> getOrderItems(
            @PathVariable Long orderId) {

        return orderItemService.getOrderItems(orderId);
    }
}