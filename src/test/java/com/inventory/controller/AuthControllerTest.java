package com.inventory.controller;

import com.inventory.dto.LoginRequest;
import com.inventory.dto.LoginResponse;
import com.inventory.dto.SignUpRequest;
import com.inventory.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    // =========================
    // LOGIN TEST
    // =========================
    @Test
    void login_ShouldReturnLoginResponse() {

        LoginRequest request = new LoginRequest();
        request.setEmail("test@mail.com");
        request.setPassword("123456");

        LoginResponse mockResponse =
                new LoginResponse("jwt-token", 1L, "USER");

        when(authService.login(request))
                .thenReturn(mockResponse);

        LoginResponse response = authController.login(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals(1L, response.getUserId());
        assertEquals("USER", response.getRole());

        verify(authService, times(1)).login(request);
    }

    // =========================
    // SIGNUP TEST
    // =========================
    @Test
    void registerUser_ShouldReturnSuccessMessage() {

        SignUpRequest request = new SignUpRequest();
        request.setUsername("john");
        request.setEmail("john@mail.com");
        request.setPassword("123456");

        when(authService.signup(request))
                .thenReturn("User registered successfully!");

        ResponseEntity<String> response =
                authController.registerUser(request);

        assertNotNull(response);

        // ✅ FIXED FOR SPRING BOOT 3+
        assertEquals(200, response.getStatusCode().value());

        assertEquals("User registered successfully!", response.getBody());

        verify(authService, times(1)).signup(request);
    }
}