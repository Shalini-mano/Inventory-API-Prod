package com.inventory.dto;
import java.util.Set;

public class AssignCategoriesRequest {
    private Set<Long> categoryIds;

    public Set<Long> getCategoryIds() { return categoryIds; }
    public void setCategoryIds(Set<Long> categoryIds) { this.categoryIds = categoryIds; }
}