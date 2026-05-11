package com.inventory.controller;

import com.inventory.dto.AssignCategoriesRequest;
import com.inventory.dto.ProductRequest;
import com.inventory.dto.ProductResponse;
import com.inventory.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    // ---------------- CREATE ----------------
    @Test
    void create_shouldReturnProduct() {

        ProductRequest request = new ProductRequest();
        ProductResponse response = mock(ProductResponse.class);

        when(productService.create(request)).thenReturn(response);

        ResponseEntity<ProductResponse> result = productController.create(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());

        verify(productService).create(request);
    }

    // ---------------- BULK CREATE ----------------
    @Test
    void bulkCreate_shouldReturnList() {

        List<ProductRequest> requests = List.of(new ProductRequest());
        List<ProductResponse> responses = List.of(mock(ProductResponse.class));

        when(productService.bulkCreate(requests)).thenReturn(responses);

        ResponseEntity<List<ProductResponse>> result =
                productController.bulkCreate(requests);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());

        verify(productService).bulkCreate(requests);
    }

    // ---------------- GET PRODUCTS ----------------
    @Test
    void getProducts_shouldReturnPage() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductResponse> page =
                new PageImpl<>(List.of(mock(ProductResponse.class)));

        when(productService.getProducts(
                eq("phone"),
                eq("electronics"),
                eq(10),
                eq(pageable)
        )).thenReturn(page);

        ResponseEntity<Page<ProductResponse>> result =
                productController.getProducts("phone", "electronics", 10, pageable);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().getContent().size());

        verify(productService).getProducts("phone", "electronics", 10, pageable);
    }

    // ---------------- GET BY ID ----------------
    @Test
    void getById_shouldReturnProduct() {

        ProductResponse response = mock(ProductResponse.class);

        when(productService.getById(1L)).thenReturn(response);

        ResponseEntity<ProductResponse> result =
                productController.getById(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());

        verify(productService).getById(1L);
    }

    // ---------------- ASSIGN CATEGORIES ----------------
    @Test
    void assignCategories_shouldReturnUpdatedProduct() {

        AssignCategoriesRequest request = new AssignCategoriesRequest();
        request.setCategoryIds(Set.of(1L, 2L));

        ProductResponse response = mock(ProductResponse.class);

        when(productService.assignCategories(1L, request.getCategoryIds()))
                .thenReturn(response);

        ResponseEntity<ProductResponse> result =
                productController.assignCategories(1L, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());

        verify(productService).assignCategories(1L, request.getCategoryIds());
    }
}