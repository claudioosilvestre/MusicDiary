package com.musicdiary.services;

import com.musicdiary.converters.UserUpdateConverter;
import com.musicdiary.dtos.ChangePasswordRequestDTO;
import com.musicdiary.dtos.UserUpdateRequestDTO;
import com.musicdiary.dtos.UserUpdateResponseDTO;
import com.musicdiary.exceptions.PasswordDoesNotMatchException;
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
public class UserServiceImplTests {

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
    void getUser_shouldThrowExceptionIfEmailIsNull() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.getUser(null));

        assertEquals("Email must be valid", exception.getMessage());
    }

    @Test
    void getUser_shouldThrowExceptionIfEmailIsEmpty() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.getUser(null));

        assertEquals("Email must be valid", exception.getMessage());
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

    @Test
    void updateProfile_shouldReturnUserUpdateResponseDTO() {

        User user = new User();
        user.setId(1L);
        user.setEmail("test@mail.com");

        UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO();
        userUpdateRequestDTO.setEmail("test@mail.com");
        userUpdateRequestDTO.setFirstName("test");
        userUpdateRequestDTO.setLastName("test1");
        userUpdateRequestDTO.setBirthDate(LocalDate.of(2026, 10, 20));

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));

        UserUpdateResponseDTO userUpdateResponseDTO = new UserUpdateResponseDTO();
        userUpdateResponseDTO.setId(1L);
        userUpdateResponseDTO.setEmail("test@mail.com");
        userUpdateResponseDTO.setFirstName("test");
        userUpdateResponseDTO.setLastName("test1");
        userUpdateResponseDTO.setBirthDate(LocalDate.of(2026, 10, 20));

        when(userRepository.save(user)).thenReturn(user);
        when(userUpdateConverter.toResponseDTO(user)).thenReturn(userUpdateResponseDTO);

        UserUpdateResponseDTO result = userService.updateProfile("test@mail.com", userUpdateRequestDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("test@mail.com", result.getEmail());
        assertEquals("test", result.getFirstName());
        assertEquals("test1", result.getLastName());
        assertEquals(LocalDate.of(2026, 10, 20), result.getBirthDate());
        verify(userRepository, times(1)).findByEmail("test@mail.com");
        verify(userRepository, times(1)).save(user);
        verify(userUpdateConverter, times(1)).toResponseDTO(user);
    }

    @Test
    void updateProfile_shouldThrowExceptionIfEmailIsNull() {

        UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateProfile(null, userUpdateRequestDTO));

        assertEquals("Email must be valid", exception.getMessage());
    }

    @Test
    void updateProfile_shouldThrowExceptionIfEmailIsBlank() {

        UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateProfile("", userUpdateRequestDTO));

        assertEquals("Email must be valid", exception.getMessage());
    }

    @Test
    void updateProfile_shouldThrowExceptionIfUserUpdateRequestDTOIsNull() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateProfile("test@mail.com", null));

        assertEquals("UserUpdateRequest must be valid", exception.getMessage());
    }

    @Test
    void updateProfile_shouldReturnExceptionIfUserNotFound() {

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUser("test@mail.com")
        );

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findByEmail("test@mail.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    void changePassword_shouldChangePassword() {

        User user = new User();
        user.setId(1L);
        user.setEmail("test@mail.com");
        user.setFirstName("test");
        user.setLastName("test1");
        user.setBirthDate(LocalDate.of(2026, 10, 20));
        user.setPasswordHash("hashPassword123");

        ChangePasswordRequestDTO changePasswordRequestDTO = new ChangePasswordRequestDTO();
        changePasswordRequestDTO.setCurrentPassword("hashPassword123");
        changePasswordRequestDTO.setNewPassword("password12345");
        changePasswordRequestDTO.setConfirmNewPassword("password12345");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("hashPassword123", user.getPasswordHash())).thenReturn(true);

        String newPassword = "passwordEncoded12345";
        when(passwordEncoder.encode("password12345")).thenReturn(newPassword);

        when(userRepository.save(user)).thenReturn(user);

        userService.changePassword("test@mail.com", changePasswordRequestDTO);

        assertEquals("passwordEncoded12345", user.getPasswordHash());
    }

    @Test
    void changePassword_shouldThrowExceptionIfEmailIsNull() {

        ChangePasswordRequestDTO changePasswordRequestDTO = new ChangePasswordRequestDTO();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.changePassword(null, changePasswordRequestDTO));

        assertEquals("Email must be valid", exception.getMessage());
    }

    @Test
    void changePassword_shouldThrowExceptionIfEmailIsEmpty() {

        ChangePasswordRequestDTO changePasswordRequestDTO = new ChangePasswordRequestDTO();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.changePassword("", changePasswordRequestDTO));

        assertEquals("Email must be valid", exception.getMessage());
    }

    @Test
    void changePassword_shouldThrowExceptionIfChangePasswordRequestDTOIsNull() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.changePassword("test@mail.com", null));

        assertEquals("ChangePasswordRequest must be valid", exception.getMessage());
    }

    @Test
    void changePassword_shouldThrowExceptionIfUserNotFound() {

        ChangePasswordRequestDTO changePasswordRequestDTO = new ChangePasswordRequestDTO();

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.changePassword("test@mail.com", changePasswordRequestDTO));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("test@mail.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    void changePassword_shouldThrowExceptionIfActualPasswordDoesNotMatch() {

        User user = new User();
        user.setEmail("test@mail.com");
        user.setPasswordHash("hashPassword123");

        ChangePasswordRequestDTO changePasswordRequestDTO = new ChangePasswordRequestDTO();
        changePasswordRequestDTO.setCurrentPassword("password12345");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(changePasswordRequestDTO.getCurrentPassword(), user.getPasswordHash())).thenReturn(false);

        PasswordDoesNotMatchException exception = assertThrows(
                PasswordDoesNotMatchException.class,
                () -> userService.changePassword("test@mail.com", changePasswordRequestDTO));

        assertEquals("Password does not match", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("test@mail.com");
        verify(passwordEncoder, times(1)).matches(changePasswordRequestDTO.getCurrentPassword(), user.getPasswordHash());
        verify(userRepository, never()).save(any());
    }

    @Test
    void changePassword_shouldThrowExceptionIfNewPasswordDoesNotMatch() {

        User user = new User();
        user.setEmail("test@mail.com");
        user.setPasswordHash("hashPassword123");

        ChangePasswordRequestDTO changePasswordRequestDTO = new ChangePasswordRequestDTO();
        changePasswordRequestDTO.setCurrentPassword("hashPassword123");
        changePasswordRequestDTO.setNewPassword("newPassword123");
        changePasswordRequestDTO.setConfirmNewPassword("wrongPassword");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(changePasswordRequestDTO.getCurrentPassword(), user.getPasswordHash())).thenReturn(true);

        PasswordDoesNotMatchException exception = assertThrows(
                PasswordDoesNotMatchException.class,
                () -> userService.changePassword("test@mail.com", changePasswordRequestDTO));

        assertEquals("Password does not match", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("test@mail.com");
        verify(passwordEncoder, times(1)).matches(changePasswordRequestDTO.getCurrentPassword(), user.getPasswordHash());
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteAccount_shouldDeleteAccount() {

        User user = new User();
        user.setEmail("test@mail.com");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));

        userService.deleteAccount("test@mail.com");

        verify(userRepository, times(1)).findByEmail("test@mail.com");
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteAccount_shouldThrowExceptionIfEmailIsNull() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.deleteAccount(null));

        assertEquals("Email must be valid", exception.getMessage());
    }

    @Test
    void deleteAccount_shouldThrowExceptionIfEmailIsEmpty() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.deleteAccount(""));

        assertEquals("Email must be valid", exception.getMessage());
    }

    @Test
    void deleteAccount_shouldThrowExceptionIfUserNotFound() {

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteAccount("test@mail.com"));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("test@mail.com");
        verify(userRepository, never()).delete(any());
    }

}
