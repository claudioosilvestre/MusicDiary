package com.musicdiary.services;

import com.musicdiary.converters.UserUpdateConverter;
import com.musicdiary.dtos.UserUpdateResponseDTO;
import com.musicdiary.exceptions.UserNotFoundException;
import com.musicdiary.models.User;
import com.musicdiary.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserUpdateConverter userUpdateConverter;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    void getUser_shouldReturnUserUpdateResponseDTO() {

        User user = new User();
        user.setId(1L);
        user.setEmail("test@mail.com");
        user.setFirstName("test");
        user.setLastName("test1");
        user.setBirthDate(LocalDate.of(2026, 10, 20));


        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));

        UserUpdateResponseDTO userUpdateResponseDTO = new UserUpdateResponseDTO();
        userUpdateResponseDTO.setEmail("test@mail.com");
        userUpdateResponseDTO.setId(1L);
        userUpdateResponseDTO.setFirstName("test");
        userUpdateResponseDTO.setLastName("test1");
        userUpdateResponseDTO.setBirthDate(LocalDate.of(2026, 10, 20));

        when(userUpdateConverter.toResponseDTO(user)).thenReturn(userUpdateResponseDTO);

        UserUpdateResponseDTO result = userService.getUser("test@mail.com");

        assertNotNull(result);
        assertEquals("test@mail.com", result.getEmail());
        assertEquals(1L, result.getId());
        assertEquals("test", result.getFirstName());
        assertEquals("test1", result.getLastName());
        assertEquals(LocalDate.of(2026, 10, 20), result.getBirthDate());
        verify(userRepository, times(1)).findByEmail("test@mail.com");
        verify(userUpdateConverter, times(1)).toResponseDTO(user);
    }

    @Test
    void getUser_shouldThrowExceptionIfUserNotFound() {

        when(userRepository.findByEmail("mail@mail.com")).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUser("mail@mail.com"));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findByEmail("mail@mail.com");
    }
}
