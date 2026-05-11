package com.inventory.repo;

import com.inventory.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void testFindByEmailIgnoreCase_found() {

        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        when(userRepository.findByEmailIgnoreCase("test@gmail.com"))
                .thenReturn(Optional.of(user));

        Optional<User> result = userRepository.findByEmailIgnoreCase("test@gmail.com");

        assertTrue(result.isPresent());
        assertEquals("test@gmail.com", result.get().getEmail());

        verify(userRepository, times(1))
                .findByEmailIgnoreCase("test@gmail.com");
    }

    @Test
    void testFindByEmailIgnoreCase_notFound() {

        when(userRepository.findByEmailIgnoreCase("notfound@gmail.com"))
                .thenReturn(Optional.empty());

        Optional<User> result = userRepository.findByEmailIgnoreCase("notfound@gmail.com");

        assertTrue(result.isEmpty());

        verify(userRepository, times(1))
                .findByEmailIgnoreCase("notfound@gmail.com");
    }
}