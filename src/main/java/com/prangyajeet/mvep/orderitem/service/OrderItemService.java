package com.prangyajeet.mvep.orderitem.service;

import com.prangyajeet.mvep.order.entity.Order;
import com.prangyajeet.mvep.order.repository.OrderRepository;
import com.prangyajeet.mvep.orderitem.dto.OrderItemRequestDTO;
import com.prangyajeet.mvep.orderitem.dto.OrderItemResponseDTO;
import com.prangyajeet.mvep.orderitem.entity.OrderItem;
import com.prangyajeet.mvep.orderitem.repository.OrderItemRepository;
import com.prangyajeet.mvep.product.entity.Product;
import com.prangyajeet.mvep.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import com.prangyajeet.mvep.orderitem.entity.OrderItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderItemService(OrderItemRepository orderItemRepository,
                            OrderRepository orderRepository,
                            ProductRepository productRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public OrderItemResponseDTO createOrderItem(
            OrderItemRequestDTO requestDTO) {

        Order order = orderRepository.findById(
                requestDTO.getOrderId())
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        Product product = productRepository.findById(
                requestDTO.getProductId())
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        OrderItem orderItem = new OrderItem();

        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(requestDTO.getQuantity());
        orderItem.setPrice(product.getPrice());

        OrderItem savedOrderItem =
                orderItemRepository.save(orderItem);

        return mapToResponseDTO(savedOrderItem);
    }

    public List<OrderItemResponseDTO> getOrderItems(Long orderId) {

        return orderItemRepository.findByOrderId(orderId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<OrderItem> getOrderItemEntities(Long orderId) {

        return orderItemRepository.findByOrderId(orderId);
    }

    private OrderItemResponseDTO mapToResponseDTO(
            OrderItem orderItem) {

        OrderItemResponseDTO responseDTO =
                new OrderItemResponseDTO();

        responseDTO.setId(orderItem.getId());

        responseDTO.setOrderId(
                orderItem.getOrder().getId());

        responseDTO.setProductId(
                orderItem.getProduct().getId());

        responseDTO.setProductName(
                orderItem.getProduct().getName());

        responseDTO.setQuantity(
                orderItem.getQuantity());

        responseDTO.setPrice(
                orderItem.getPrice());

        BigDecimal totalPrice =
                orderItem.getPrice()
                        .multiply(
                                BigDecimal.valueOf(
                                        orderItem.getQuantity()));

        responseDTO.setTotalPrice(totalPrice);

        return responseDTO;
    }
}