package com.inventory.service;

import com.inventory.config.JwtUtil;
import com.inventory.dto.LoginRequest;
import com.inventory.dto.LoginResponse;
import com.inventory.dto.SignUpRequest;
import com.inventory.exception.InvalidCredentialsException;
import com.inventory.exception.UserAlreadyExistsException;
import com.inventory.model.Role;
import com.inventory.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.inventory.repo.UserRepository;

@Service
public class AuthService {

    // what're the dependencies?
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.getEmail())
            .orElseThrow(() -> new InvalidCredentialsException());

        boolean ok = bCryptPasswordEncoder.matches(request.getPassword(), user.getPasswordHash());
        if(!ok) {
            throw new InvalidCredentialsException();
        }

        // generate then Jwt Token
        String token = jwtUtil.generateToken(user.getId(), user.getRole());
        System.out.println(token);
        return new LoginResponse(token, user.getId(), user.getRole().name());
    }
    public String signup(SignUpRequest request) {
        if (userRepository.findByEmailIgnoreCase(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email is already in use.");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
        User user = new User(
                request.getUsername(),
                request.getEmail().toLowerCase(),
                encodedPassword,
                Role.USER
        );
        userRepository.save(user);

        return "User registered successfully!";
    }
}
