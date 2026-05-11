package com.inventory.controller;

import com.inventory.dto.OrderRequest;
import com.inventory.dto.OrderResponse;
import com.inventory.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setupSecurityContext() {

        // IMPORTANT: no mocking of authentication methods needed
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("1", null, List.of())
        );
    }

    @Test
    void createOrder_shouldReturnCreatedOrder() {

        OrderRequest request = new OrderRequest();
        OrderResponse response = mock(OrderResponse.class);

        when(orderService.placeOrder(any(), eq(1L))).thenReturn(response);

        ResponseEntity<OrderResponse> result = orderController.createOrder(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());

        verify(orderService, times(1)).placeOrder(any(), eq(1L));
    }

    @Test
    void getMyOrders_shouldReturnList() {

        List<OrderResponse> mockList = List.of(mock(OrderResponse.class));

        when(orderService.getOrdersByUser(1L)).thenReturn(mockList);

        ResponseEntity<List<OrderResponse>> result = orderController.getMyOrders();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());

        verify(orderService).getOrdersByUser(1L);
    }

    @Test
    void cancelOrder_shouldReturnOrder() {

        com.inventory.model.Order order = mock(com.inventory.model.Order.class);

        when(orderService.cancelOrder(10L)).thenReturn(order);

        ResponseEntity<com.inventory.model.Order> result =
                orderController.cancel(10L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());

        verify(orderService).cancelOrder(10L);
    }
}