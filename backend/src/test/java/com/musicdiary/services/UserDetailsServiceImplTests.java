package com.musicdiary.services;

import com.musicdiary.models.User;
import com.musicdiary.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername_withValidData_shouldReturnUserDetails() {

        User user = new User();
        user.setEmail("test@email.com");

        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("test@email.com");

        assertNotNull(userDetails);
        verify(userRepository, times(1)).findByEmail("test@email.com");
        assertEquals("test@email.com", userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_shouldThrowExceptionIfEmailIsNull() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userDetailsService.loadUserByUsername(null));

        assertEquals("Email must be valid", exception.getMessage());
    }

    @Test
    void loadUserByUsername_shouldThrowExceptionIfEmailIsEmpty() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userDetailsService.loadUserByUsername(""));

        assertEquals("Email must be valid", exception.getMessage());
    }
    
     @Test
    void loadUserByUsername_withInvalidData_shouldThrowException() {

        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("test@email.com"));

        verify(userRepository).findByEmail("test@email.com");
        assertEquals("User not found: test@email.com", exception.getMessage());
    }
}
