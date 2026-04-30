package com.inventory.dto;

import com.inventory.model.ProductStatus;

import java.util.Set;

public class ProductRequest {

    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private ProductStatus status;

    private Set<Long> categoryIds; // IMPORTANT: avoid sending full Category entity

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }

    public ProductStatus getStatus() { return status; }
    public void setStatus(ProductStatus status) { this.status = status; }

    public Set<Long> getCategoryIds() { return categoryIds; }
    public void setCategoryIds(Set<Long> categoryIds) { this.categoryIds = categoryIds; }


}