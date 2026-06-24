package com.musicdiary.services;

import com.musicdiary.converters.UserUpdateConverter;
import com.musicdiary.dtos.UserUpdateRequestDTO;
import com.musicdiary.dtos.UserUpdateResponseDTO;
import com.musicdiary.exceptions.UserNotFoundException;
import com.musicdiary.models.User;
import com.musicdiary.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserUpdateConverter userUpdateConverter;

    public UserServiceImpl(UserRepository userRepository, UserUpdateConverter userUpdateConverter) {
        this.userRepository = userRepository;
        this.userUpdateConverter = userUpdateConverter;
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
    public void deleteAccount(String email) {
        if(email == null) {
            throw new IllegalArgumentException("Email must be valid");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException());

        userRepository.delete(user);

    }
}
