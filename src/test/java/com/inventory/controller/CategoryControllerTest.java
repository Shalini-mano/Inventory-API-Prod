package com.inventory.controller;

import com.inventory.dto.CategoryRequest;
import com.inventory.dto.CategoryResponse;
import com.inventory.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    // =========================
    // CREATE
    // =========================
    @Test
    void create_ShouldReturnCategory() {

        CategoryRequest request = new CategoryRequest();
        request.setName("Electronics");

        CategoryResponse response =
                new CategoryResponse(1L, "Electronics");

        when(categoryService.create(request))
                .thenReturn(response);

        ResponseEntity<CategoryResponse> result =
                categoryController.create(request);

        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertEquals("Electronics", result.getBody().getName());

        verify(categoryService, times(1)).create(request);
    }

    // =========================
    // UPDATE
    // =========================
    @Test
    void update_ShouldReturnUpdatedCategory() {

        CategoryRequest request = new CategoryRequest();
        request.setName("Updated Category");

        CategoryResponse response =
                new CategoryResponse(1L, "Updated Category");

        when(categoryService.update(1L, request))
                .thenReturn(response);

        ResponseEntity<CategoryResponse> result =
                categoryController.update(1L, request);

        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertEquals("Updated Category", result.getBody().getName());

        verify(categoryService, times(1)).update(1L, request);
    }

    // =========================
    // DELETE
    // =========================
    @Test
    void delete_ShouldReturnNoContent() {

        doNothing().when(categoryService).delete(1L);

        ResponseEntity<Void> response =
                categoryController.delete(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());

        verify(categoryService, times(1)).delete(1L);
    }

    // =========================
    // GET ALL
    // =========================
    @Test
    void getAll_ShouldReturnList() {

        List<CategoryResponse> list = List.of(
                new CategoryResponse(1L, "Electronics"),
                new CategoryResponse(2L, "Books")
        );

        when(categoryService.getAll())
                .thenReturn(list);

        ResponseEntity<List<CategoryResponse>> response =
                categoryController.getAll();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());

        verify(categoryService, times(1)).getAll();
    }

    // =========================
    // GET BY ID
    // =========================
    @Test
    void getById_ShouldReturnCategory() {

        CategoryResponse response =
                new CategoryResponse(1L, "Electronics");

        when(categoryService.getById(1L))
                .thenReturn(response);

        ResponseEntity<CategoryResponse> result =
                categoryController.getById(1L);

        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertEquals("Electronics", result.getBody().getName());

        verify(categoryService, times(1)).getById(1L);
    }
}