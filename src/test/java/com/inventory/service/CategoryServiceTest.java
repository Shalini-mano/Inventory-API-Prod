package com.inventory.service;

import com.inventory.dto.CategoryRequest;
import com.inventory.dto.CategoryResponse;
import com.inventory.model.Category;
import com.inventory.repo.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {

        category = new Category();
        category.setId(1L);
        category.setName("Electronics");
    }

    @Test
    void create_ShouldCreateCategorySuccessfully() {

        CategoryRequest request = new CategoryRequest();
        request.setName("Electronics");

        when(categoryRepository.save(any(Category.class)))
                .thenReturn(category);

        CategoryResponse response = categoryService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Electronics", response.getName());

        verify(categoryRepository, times(1))
                .save(any(Category.class));
    }

    @Test
    void update_ShouldUpdateCategorySuccessfully() {

        CategoryRequest request = new CategoryRequest();
        request.setName("Updated Electronics");

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(categoryRepository.save(any(Category.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CategoryResponse response = categoryService.update(1L, request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Updated Electronics", response.getName());

        verify(categoryRepository, times(1))
                .findById(1L);

        verify(categoryRepository, times(1))
                .save(any(Category.class));
    }

    @Test
    void update_ShouldThrowException_WhenCategoryNotFound() {

        CategoryRequest request = new CategoryRequest();
        request.setName("Updated Electronics");

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> categoryService.update(1L, request)
        );

        assertEquals("Category not found", exception.getMessage());

        verify(categoryRepository, times(1))
                .findById(1L);

        verify(categoryRepository, never())
                .save(any(Category.class));
    }

    @Test
    void delete_ShouldDeleteCategorySuccessfully() {

        when(categoryRepository.existsById(1L))
                .thenReturn(true);

        doNothing().when(categoryRepository).deleteById(1L);

        categoryService.delete(1L);

        verify(categoryRepository, times(1))
                .existsById(1L);

        verify(categoryRepository, times(1))
                .deleteById(1L);
    }

    @Test
    void delete_ShouldThrowException_WhenCategoryNotFound() {

        when(categoryRepository.existsById(1L))
                .thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> categoryService.delete(1L)
        );

        assertEquals("Category not found", exception.getMessage());

        verify(categoryRepository, times(1))
                .existsById(1L);

        verify(categoryRepository, never())
                .deleteById(anyLong());
    }

    @Test
    void getAll_ShouldReturnAllCategories() {

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Furniture");

        when(categoryRepository.findAll())
                .thenReturn(Arrays.asList(category, category2));

        List<CategoryResponse> responses = categoryService.getAll();

        assertNotNull(responses);
        assertEquals(2, responses.size());

        assertEquals("Electronics", responses.get(0).getName());
        assertEquals("Furniture", responses.get(1).getName());

        verify(categoryRepository, times(1))
                .findAll();
    }

    @Test
    void getById_ShouldReturnCategory_WhenCategoryExists() {

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        CategoryResponse response = categoryService.getById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Electronics", response.getName());

        verify(categoryRepository, times(1))
                .findById(1L);
    }

    @Test
    void getById_ShouldThrowException_WhenCategoryNotFound() {

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> categoryService.getById(1L)
        );

        assertEquals("Category not found", exception.getMessage());

        verify(categoryRepository, times(1))
                .findById(1L);
    }
}