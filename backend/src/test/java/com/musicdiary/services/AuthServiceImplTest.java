package com.musicdiary.services;

import com.musicdiary.dtos.LoginRequestDTO;
import com.musicdiary.dtos.RegisterRequestDTO;
import com.musicdiary.exceptions.EmailNotFoundException;
import com.musicdiary.exceptions.PasswordDoesNotMatchException;
import com.musicdiary.exceptions.UserEmailAlreadyExistsException;
import com.musicdiary.models.User;
import com.musicdiary.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

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
        registerRequestDTO.setEmail("test@mail.com");
        registerRequestDTO.setFirstName("testName");
        registerRequestDTO.setLastName("testLastName");
        registerRequestDTO.setBirthDate(LocalDate.of(1990, 10, 20));
        registerRequestDTO.setPassword("123456789");

        String passwordHash = "hashedPassword123";

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456789")).thenReturn(passwordHash);

        ArgumentCaptor<User> user = ArgumentCaptor.forClass(User.class);

        when(jwtService.generateToken(any(User.class))).thenReturn("fake-jwt-token");

        String result = authService.register(registerRequestDTO);

        verify(userRepository).save(user.capture());

        User savedUser = user.getValue();

        assertEquals("fake-jwt-token", result);
        assertEquals("hashedPassword123", savedUser.getPasswordHash());
        assertEquals("test@mail.com", savedUser.getEmail());
        assertEquals("testName", savedUser.getFirstName());
        assertEquals("testLastName", savedUser.getLastName());
        assertEquals(LocalDate.of(1990, 10, 20), savedUser.getBirthDate());
    }

    @Test
    void registerWithEmailAlreadyRegistered_shouldThrowException() {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setEmail("test@mail.com");

        User user = new User();

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));

        UserEmailAlreadyExistsException exception = assertThrows(
                UserEmailAlreadyExistsException.class,
                () -> authService.register(registerRequestDTO));

        verify(userRepository).findByEmail("test@mail.com");
        assertEquals("Email already exists", exception.getMessage());
    }

    @Test
    void loginWithValidData_shouldReturnGeneratedToken() {

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("test@mail.com");
        loginRequestDTO.setPassword("123456789");

        User user = new User();
        user.setEmail("test@mail.com");
        user.setPasswordHash("hash-password123");
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));

        when(passwordEncoder.matches("123456789", "hash-password123")).thenReturn(true);

        when(jwtService.generateToken(any(User.class))).thenReturn("fake-jwt-token");

        String login = authService.login(loginRequestDTO);

        verify(jwtService, times(1)).generateToken(user);
        assertEquals("fake-jwt-token", login);
    }

    @Test
    void loginWithInvalidEmail_shouldThrowException () {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("test@mail.com");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.empty());

        EmailNotFoundException exception = assertThrows(
                EmailNotFoundException.class,
                () -> authService.login(loginRequestDTO));

        verify(userRepository).findByEmail("test@mail.com");
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(jwtService);
        assertEquals("Email not found", exception.getMessage());
    }

    @Test
    void loginWithInvalidPassword_shouldThrowException() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("test@mail.com");
        loginRequestDTO.setPassword("123456789");

        User user = new User();
        user.setPasswordHash("hashedPassword123");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(false);

        PasswordDoesNotMatchException exception = assertThrows(
                PasswordDoesNotMatchException.class,
                () -> authService.login(loginRequestDTO));

        verify(userRepository).findByEmail("test@mail.com");
        verifyNoInteractions(jwtService);
        assertEquals("Password does not match", exception.getMessage());
    }
}
