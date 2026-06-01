package com.musicdiary.services;

import com.musicdiary.dtos.RegisterRequestDTO;
import com.musicdiary.models.User;
import com.musicdiary.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;


    @Test
    void registerWithValidData_shouldReturnGeneratedToken() {

        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setFirstName("testName");
        registerRequestDTO.setLastName("testLastName");
        registerRequestDTO.setEmail("test@email.com");
        registerRequestDTO.setBirthDate(LocalDate.of(1990, 01, 01));
        registerRequestDTO.setPassword("123456789");

        when(passwordEncoder.encode("123456789")).thenReturn("hashedPassword123");

        User user = new User();
        user.setFirstName("testName");
        user.setLastName("testLastName");
        user.setEmail("test@email.com");
        user.setBirthDate(LocalDate.of(1990, 01, 01));
        user.setPasswordHash("hashedPassword123");

        when(userRepository.save(any(User.class))).thenReturn(user);

        when(jwtService.generateToken(any(User.class))).thenReturn("fake-jwt-token");

        String string = authService.register(registerRequestDTO);

        assertEquals("fake-jwt-token", string);
    }

}
