package com.inventory.repo;

import com.inventory.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @Mock
    private ProductRepository productRepository;

    // ---------------- TEST 1 ----------------
    @Test
    void findByNameContainingIgnoreCase_shouldReturnProducts() {

        Pageable pageable = PageRequest.of(0, 10);

        Product product = new Product();
        product.setName("iPhone");

        Page<Product> mockPage =
                new PageImpl<>(List.of(product));

        when(productRepository.findByNameContainingIgnoreCase("iphone", pageable))
                .thenReturn(mockPage);

        Page<Product> result =
                productRepository.findByNameContainingIgnoreCase("iphone", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("iPhone", result.getContent().get(0).getName());

        verify(productRepository, times(1))
                .findByNameContainingIgnoreCase("iphone", pageable);
    }

    // ---------------- TEST 2 ----------------
    @Test
    void findByCategories_Name_shouldReturnProducts() {

        Pageable pageable = PageRequest.of(0, 10);

        Product product = new Product();
        product.setName("Laptop");

        Page<Product> mockPage =
                new PageImpl<>(List.of(product));

        when(productRepository.findByCategories_Name("electronics", pageable))
                .thenReturn(mockPage);

        Page<Product> result =
                productRepository.findByCategories_Name("electronics", pageable);

        assertEquals(1, result.getContent().size());

        verify(productRepository).findByCategories_Name("electronics", pageable);
    }

    // ---------------- TEST 3 ----------------
    @Test
    void findByStockQuantityLessThan_shouldReturnLowStockProducts() {

        Pageable pageable = PageRequest.of(0, 10);

        Product product = new Product();
        product.setStockQuantity(5);

        Page<Product> mockPage =
                new PageImpl<>(List.of(product));

        when(productRepository.findByStockQuantityLessThan(10, pageable))
                .thenReturn(mockPage);

        Page<Product> result =
                productRepository.findByStockQuantityLessThan(10, pageable);

        assertEquals(5, result.getContent().get(0).getStockQuantity());

        verify(productRepository).findByStockQuantityLessThan(10, pageable);
    }
}