package com.inventory.service;

import com.inventory.config.JwtUtil;
import com.inventory.dto.LoginRequest;
import com.inventory.dto.LoginResponse;
import com.inventory.dto.SignUpRequest;
import com.inventory.exception.InvalidCredentialsException;
import com.inventory.exception.UserAlreadyExistsException;
import com.inventory.model.Role;
import com.inventory.model.User;
import com.inventory.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private User user;

    @BeforeEach
    void setUp() {

        user = new User(
                "John",
                "john@test.com",
                "hashedPassword",
                Role.USER
        );

        user.setId(1L);
    }

    @Test
    void login_ShouldReturnToken_WhenCredentialsAreValid() {

        LoginRequest request = new LoginRequest();
        request.setEmail("john@test.com");
        request.setPassword("password123");

        when(userRepository.findByEmailIgnoreCase("john@test.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("password123", "hashedPassword"))
                .thenReturn(true);

        when(jwtUtil.generateToken(anyLong(), eq(Role.USER)))
                .thenReturn("jwt-token");

        LoginResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals(1L, response.getUserId());
        assertEquals("USER", response.getRole());

        verify(userRepository, times(1))
                .findByEmailIgnoreCase("john@test.com");

        verify(passwordEncoder, times(1))
                .matches("password123", "hashedPassword");

        verify(jwtUtil, times(1))
                .generateToken(1L, Role.USER);
    }

    @Test
    void login_ShouldThrowException_WhenUserNotFound() {

        LoginRequest request = new LoginRequest();
        request.setEmail("unknown@test.com");
        request.setPassword("password123");

        when(userRepository.findByEmailIgnoreCase("unknown@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );

        verify(userRepository, times(1))
                .findByEmailIgnoreCase("unknown@test.com");

        verify(passwordEncoder, never())
                .matches(anyString(), anyString());

        verify(jwtUtil, never())
                .generateToken(anyLong(), any(Role.class));
    }

    @Test
    void login_ShouldThrowException_WhenPasswordIsIncorrect() {

        LoginRequest request = new LoginRequest();
        request.setEmail("john@test.com");
        request.setPassword("wrongpassword");

        when(userRepository.findByEmailIgnoreCase("john@test.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrongpassword", "hashedPassword"))
                .thenReturn(false);

        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );

        verify(userRepository, times(1))
                .findByEmailIgnoreCase("john@test.com");

        verify(passwordEncoder, times(1))
                .matches("wrongpassword", "hashedPassword");

        verify(jwtUtil, never())
                .generateToken(anyLong(), any(Role.class));
    }

    @Test
    void signup_ShouldRegisterUser_WhenEmailDoesNotExist() {

        SignUpRequest request = new SignUpRequest();
        request.setUsername("John");
        request.setEmail("john@test.com");
        request.setPassword("password123");

        when(userRepository.findByEmailIgnoreCase("john@test.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        String result = authService.signup(request);

        assertEquals("User registered successfully!", result);

        verify(userRepository, times(1))
                .findByEmailIgnoreCase("john@test.com");

        verify(passwordEncoder, times(1))
                .encode("password123");

        verify(userRepository, times(1))
                .save(any(User.class));
    }

    @Test
    void signup_ShouldThrowException_WhenEmailAlreadyExists() {

        SignUpRequest request = new SignUpRequest();
        request.setUsername("John");
        request.setEmail("john@test.com");
        request.setPassword("password123");

        when(userRepository.findByEmailIgnoreCase("john@test.com"))
                .thenReturn(Optional.of(user));

        assertThrows(
                UserAlreadyExistsException.class,
                () -> authService.signup(request)
        );

        verify(userRepository, times(1))
                .findByEmailIgnoreCase("john@test.com");

        verify(passwordEncoder, never())
                .encode(anyString());

        verify(userRepository, never())
                .save(any(User.class));
    }
}