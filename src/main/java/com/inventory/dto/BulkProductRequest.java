package com.inventory.dto;


import java.util.List;

public class BulkProductRequest {
    private List<ProductRequest> products;

    public List<ProductRequest> getProducts() {
        return products;
    }

    public void setProducts(List<ProductRequest> products) {
        this.products = products;
    }
}