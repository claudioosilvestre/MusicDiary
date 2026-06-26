package com.musicdiary.services;

import com.musicdiary.converters.UserUpdateConverter;
import com.musicdiary.dtos.ChangePasswordRequestDTO;
import com.musicdiary.dtos.UserUpdateRequestDTO;
import com.musicdiary.dtos.UserUpdateResponseDTO;
import com.musicdiary.exceptions.PasswordDoesNotMatchException;
import com.musicdiary.exceptions.UserNotFoundException;
import com.musicdiary.models.User;
import com.musicdiary.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserUpdateConverter userUpdateConverter;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(PasswordEncoder passwordEncoder, UserUpdateConverter userUpdateConverter, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userUpdateConverter = userUpdateConverter;
        this.userRepository = userRepository;
    }

    @Override
    public UserUpdateResponseDTO getUser(String email) {

        if(email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must be valid");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException());

        return userUpdateConverter.toResponseDTO(user);
    }

    @Override
    public UserUpdateResponseDTO updateProfile(String email, UserUpdateRequestDTO userUpdateRequestDTO) {

        if(email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must be valid");
        }
        if(userUpdateRequestDTO == null) {
            throw new IllegalArgumentException("UserUpdateRequest must be valid");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException());

        user.setFirstName(userUpdateRequestDTO.getFirstName());
        user.setLastName(userUpdateRequestDTO.getLastName());
        user.setEmail(userUpdateRequestDTO.getEmail());
        user.setBirthDate(userUpdateRequestDTO.getBirthDate());

        userRepository.save(user);

        return userUpdateConverter.toResponseDTO(user);
    }

    @Override
    public void changePassword(String email, ChangePasswordRequestDTO changePasswordRequestDTO) {

        if(email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must be valid");
        }
        if(changePasswordRequestDTO == null) {
            throw new IllegalArgumentException("ChangePasswordRequest must be valid");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException());

        Boolean passwordMatches = passwordEncoder.matches(changePasswordRequestDTO.getCurrentPassword(), user.getPasswordHash());

        if (!passwordMatches) {
            throw new PasswordDoesNotMatchException();
        }

        if(!changePasswordRequestDTO.getNewPassword().equals(changePasswordRequestDTO.getConfirmNewPassword())) {
            throw new PasswordDoesNotMatchException();
        }

        String newPassword = passwordEncoder.encode(changePasswordRequestDTO.getNewPassword());

        user.setPasswordHash(newPassword);
        userRepository.save(user);
    }

    @Override
    public void deleteAccount(String email) {
        if(email == null) {
            throw new IllegalArgumentException("Email must be valid");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException());

        userRepository.delete(user);
    }
}
