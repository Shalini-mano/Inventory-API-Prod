package com.inventory.repo;

import com.inventory.model.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryTest {

    @Mock
    private OrderRepository orderRepository;

    @Test
    void testFindByUserId() {

        Long userId = 1L;

        Order order = new Order();

        List<Order> mockList = List.of(order);

        when(orderRepository.findByUserId(userId)).thenReturn(mockList);

        List<Order> result = orderRepository.findByUserId(userId);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(orderRepository, times(1)).findByUserId(userId);
    }
}