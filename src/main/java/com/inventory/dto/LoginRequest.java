package com.inventory.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

public class LoginRequest {
    @NotBlank(message = "email is required")
    @Email(message = "email is invalid")
    private String email;

    @NotBlank(message = "password is required")
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static class OrderResponse {

        private Long id;
        private String status;
        private LocalDateTime orderDate;
        private List<OrderItemResponse> items;

        public OrderResponse(Long id, String status, LocalDateTime orderDate, List<OrderItemResponse> items) {
            this.id = id;
            this.status = status;
            this.orderDate = orderDate;
            this.items = items;
        }

        // getters
    }
}
