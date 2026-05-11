package com.inventory.service;

import com.inventory.dto.ProductRequest;
import com.inventory.dto.ProductResponse;
import com.inventory.model.Category;
import com.inventory.model.Product;
import com.inventory.model.ProductStatus;
import com.inventory.repo.CategoryRepository;
import com.inventory.repo.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private Category category;
    private Product product;

    @BeforeEach
    void setUp() {

        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        product = new Product();
        product.setName("Laptop");
        product.setDescription("Gaming Laptop");
        product.setPrice(50000.0);
        product.setStockQuantity(10);
        product.setStatus(ProductStatus.ACTIVE);
        product.setCategories(Set.of(category));
    }

    // =========================
    // CREATE PRODUCT
    // =========================
    @Test
    void create_ShouldCreateProductSuccessfully() {

        ProductRequest request = new ProductRequest();
        request.setName("Laptop");
        request.setDescription("Gaming Laptop");
        request.setPrice(50000.0);
        request.setStockQuantity(10);
        request.setStatus(ProductStatus.ACTIVE);
        request.setCategoryIds(Set.of(1L));

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponse response = productService.create(request);

        assertNotNull(response);
        assertEquals("Laptop", response.getName());

        verify(productRepository, times(1)).save(any(Product.class));
    }

    // =========================
    // BULK CREATE
    // =========================
    @Test
    void bulkCreate_ShouldCreateMultipleProducts() {

        ProductRequest request1 = new ProductRequest();
        request1.setName("Laptop");
        request1.setDescription("Gaming");
        request1.setPrice(50000.0);
        request1.setStockQuantity(10);
        request1.setStatus(ProductStatus.ACTIVE);
        request1.setCategoryIds(Set.of(1L));

        ProductRequest request2 = new ProductRequest();
        request2.setName("Mouse");
        request2.setDescription("Wireless");
        request2.setPrice(1000.0);
        request2.setStockQuantity(50);
        request2.setStatus(ProductStatus.ACTIVE);
        request2.setCategoryIds(Set.of(1L));

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<ProductResponse> responses =
                productService.bulkCreate(List.of(request1, request2));

        assertEquals(2, responses.size());

        verify(productRepository, times(2))
                .save(any(Product.class));
    }

    // =========================
    // GET BY ID
    // =========================
    @Test
    void getById_ShouldReturnProduct() {

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        ProductResponse response = productService.getById(1L);

        assertNotNull(response);
        assertEquals("Laptop", response.getName());

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getById_ShouldThrow_WhenNotFound() {

        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> productService.getById(1L)
        );

        assertTrue(ex.getMessage().contains("Product not found"));

        verify(productRepository, times(1)).findById(1L);
    }

    // =========================
    // ASSIGN CATEGORIES
    // =========================
    @Test
    void assignCategories_ShouldUpdateCategories() {

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponse response =
                productService.assignCategories(1L, Set.of(1L));

        assertNotNull(response);
        assertEquals("Laptop", response.getName());

        verify(productRepository).save(any(Product.class));
    }

    // =========================
    // PAGINATION (GET PRODUCTS)
    // =========================
    @Test
    void getProducts_ShouldReturnPage() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> page =
                new PageImpl<>(List.of(product));

        when(productRepository.findAll(pageable))
                .thenReturn(page);

        Page<ProductResponse> result =
                productService.getProducts(null, null, null, pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("Laptop", result.getContent().get(0).getName());

        verify(productRepository, times(1)).findAll(pageable);
    }
}