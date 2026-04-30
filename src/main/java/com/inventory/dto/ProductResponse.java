package com.inventory.dto;

import com.inventory.model.ProductStatus;

import java.util.Set;

public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private ProductStatus status;

    private Set<CategoryDto> categories;

    public ProductResponse(Long id, String name, String description,
                           Double price, Integer stockQuantity,
                           ProductStatus status,
                           Set<CategoryDto> categories) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.status = status;
        this.categories = categories;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Double getPrice() { return price; }
    public Integer getStockQuantity() { return stockQuantity; }
    public ProductStatus getStatus() { return status; }
    public Set<CategoryDto> getCategories() { return categories; }
}