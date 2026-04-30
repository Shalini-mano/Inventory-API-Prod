package com.inventory.controller;

import com.inventory.dto.AssignCategoriesRequest;
import com.inventory.dto.ProductRequest;
import com.inventory.dto.ProductResponse;
import com.inventory.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create-product")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.create(request));
    }

    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductResponse>> bulkCreate(
            @RequestBody List<ProductRequest> requests) {
        return ResponseEntity.ok(productService.bulkCreate(requests));
    }

    // ✅ SINGLE GET ENDPOINT (FIXED)
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer lowStock,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                productService.getProducts(name, category, lowStock, pageable)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @PutMapping("/{id}/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> assignCategories(
            @PathVariable Long id,
            @RequestBody AssignCategoriesRequest request
    ) {
        return ResponseEntity.ok(
                productService.assignCategories(id, request.getCategoryIds())
        );
    }
}