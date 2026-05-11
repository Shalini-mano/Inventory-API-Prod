package com.inventory.service;

import com.inventory.dto.OrderItemRequest;
import com.inventory.dto.OrderRequest;
import com.inventory.dto.OrderResponse;
import com.inventory.exception.InsufficientStockException;
import com.inventory.exception.ResourceNotFoundException;
import com.inventory.model.*;
import com.inventory.repo.OrderRepository;
import com.inventory.repo.ProductRepository;
import com.inventory.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private Product product;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);
        user.setUsername("John");

        product = new Product();
        product.setId(100L);
        product.setName("Laptop");
        product.setPrice(50000.0);
        product.setStockQuantity(10);
    }

    @Test
    void placeOrder_ShouldCreateOrderSuccessfully() {

        OrderItemRequest item = new OrderItemRequest();
        item.setProductId(100L);
        item.setQuantity(2);

        OrderRequest request = new OrderRequest();
        request.setItems(List.of(item));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(productRepository.findById(100L))
                .thenReturn(Optional.of(product));

        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = orderService.placeOrder(request, 1L);

        assertNotNull(response);
        assertEquals("CONFIRMED", response.getStatus());

        verify(userRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(100L);
        verify(productRepository, times(1)).save(any(Product.class));
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void placeOrder_ShouldThrow_WhenUserNotFound() {

        OrderRequest request = new OrderRequest();
        request.setItems(List.of());

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.placeOrder(request, 1L));

        verifyNoInteractions(productRepository);
        verifyNoInteractions(orderRepository);
    }

    @Test
    void placeOrder_ShouldThrow_WhenProductNotFound() {

        OrderItemRequest item = new OrderItemRequest();
        item.setProductId(100L);
        item.setQuantity(1);

        OrderRequest request = new OrderRequest();
        request.setItems(List.of(item));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(productRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.placeOrder(request, 1L));

        verify(orderRepository, never()).save(any());
    }

    @Test
    void placeOrder_ShouldThrow_WhenStockIsInsufficient() {

        product.setStockQuantity(1);

        OrderItemRequest item = new OrderItemRequest();
        item.setProductId(100L);
        item.setQuantity(5);

        OrderRequest request = new OrderRequest();
        request.setItems(List.of(item));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(productRepository.findById(100L))
                .thenReturn(Optional.of(product));

        assertThrows(InsufficientStockException.class,
                () -> orderService.placeOrder(request, 1L));

        verify(productRepository, never()).save(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void getOrdersByUser_ShouldReturnOrders() {

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(2);
        item.setPrice(50000.0);

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setItems(List.of(item));

        when(orderRepository.findByUserId(1L))
                .thenReturn(List.of(order));

        List<OrderResponse> responses = orderService.getOrdersByUser(1L);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("CONFIRMED", responses.get(0).getStatus());

        verify(orderRepository, times(1)).findByUserId(1L);
    }

    @Test
    void cancelOrder_ShouldCancelSuccessfully() {

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(2);

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setItems(List.of(item));

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.cancelOrder(1L);

        assertEquals(OrderStatus.CANCELLED, result.getStatus());

        verify(productRepository, times(1)).save(any(Product.class));
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void cancelOrder_ShouldThrow_WhenOrderNotFound() {

        when(orderRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.cancelOrder(1L));

        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, never()).save(any());
    }
}