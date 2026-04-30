package com.inventory.service;

import com.inventory.dto.OrderItemResponse;
import com.inventory.dto.OrderResponse;
import com.inventory.model.*;
import com.inventory.dto.OrderRequest;
import com.inventory.dto.OrderItemRequest;
import com.inventory.exception.InsufficientStockException;
import com.inventory.exception.ResourceNotFoundException;
import com.inventory.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.inventory.repo.OrderRepository;
import com.inventory.repo.ProductRepository;
import com.inventory.repo.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public OrderResponse placeOrder(OrderRequest request, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = new Order(
                user,
                LocalDateTime.now(),
                OrderStatus.CREATED
        );

        List<OrderItem> items = new ArrayList<>();
        List<OrderItemResponse> itemResponses = new ArrayList<>();

        double total = 0;

        for (OrderItemRequest itemDto : request.getItems()) {

            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product not found ID: " + itemDto.getProductId()));

            if (product.getStockQuantity() < itemDto.getQuantity()) {
                throw new InsufficientStockException(
                        "Not enough stock for: " + product.getName());
            }

            // reduce stock
            product.setStockQuantity(product.getStockQuantity() - itemDto.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem(
                    order,
                    product,
                    itemDto.getQuantity(),
                    product.getPrice()
            );

            items.add(orderItem);

            // build response DTO
            itemResponses.add(new OrderItemResponse(
                    product.getId(),
                    product.getName(),
                    itemDto.getQuantity(),
                    product.getPrice()
            ));

            total += product.getPrice() * itemDto.getQuantity();
        }

        order.setItems(items);
        order.setTotalAmount(total);
        order.setStatus(OrderStatus.CONFIRMED);

        Order savedOrder = orderRepository.save(order);

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getStatus().name(),
                savedOrder.getOrderDate(),
                savedOrder.getTotalAmount(),
                itemResponses
        );
    }
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUser(Long userId) {

        return orderRepository.findByUserId(userId)
                .stream()
                .map(order -> {

                    List<OrderItemResponse> items = order.getItems().stream()
                            .map(item -> new OrderItemResponse(
                                    item.getProduct().getId(),
                                    item.getProduct().getName(),
                                    item.getQuantity(),
                                    item.getPrice()
                            ))
                            .toList();

                    return new OrderResponse(
                            order.getId(),
                            order.getStatus().name(),
                            order.getOrderDate(),
                            order.getTotalAmount(),
                            items
                    );
                })
                .toList();
    }


    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order is already cancelled.");
        }

        // Restore Stock levels
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

}