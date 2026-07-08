package com.musicdiary.services;

import com.musicdiary.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceImplTests {

    @InjectMocks
    private JwtServiceImpl jwtService;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(jwtService, "secret", "test-secret-key-minimum-32-characters!!");
        ReflectionTestUtils.setField(jwtService, "expiration", 3600000L);
    }

    @Test
    void generateTokenWithValidUser_shouldReturnToken() {

        User user = new User();
        user.setEmail("test@email.com");

        String token = jwtService.generateToken(user);

        assertNotNull(token);
    }

    @Test
    void extractEmailWithValidToken_shouldReturnEmail() {

        User user = new User();
        user.setEmail("test@email.com");

        String token = jwtService.generateToken(user);

        String email = jwtService.extractEmail(token);

        assertNotNull(email);
        assertEquals("test@email.com", email);
    }

    @Test
    void extractEmailWithInvalidToken_shouldThrowException() {

        String email = jwtService.extractEmail("invalidtoken12345");

        assertNull(email);
    }
}
