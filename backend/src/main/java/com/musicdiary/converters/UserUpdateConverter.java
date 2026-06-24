package com.musicdiary.converters;

import com.musicdiary.dtos.UserUpdateRequestDTO;
import com.musicdiary.dtos.UserUpdateResponseDTO;
import com.musicdiary.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserUpdateConverter {

    public UserUpdateResponseDTO toResponseDTO (User user) {

        UserUpdateResponseDTO userUpdateResponseDTO = new UserUpdateResponseDTO();
        userUpdateResponseDTO.setId(user.getId());
        userUpdateResponseDTO.setFirstName(user.getFirstName());
        userUpdateResponseDTO.setLastName(user.getLastName());
        userUpdateResponseDTO.setEmail(user.getEmail());
        userUpdateResponseDTO.setBirthDate(user.getBirthDate());

        return userUpdateResponseDTO;
    }

    public User toEntity (UserUpdateRequestDTO userUpdateRequestDTO) {

        User user = new User();
        user.setFirstName(userUpdateRequestDTO.getFirstName());
        user.setLastName(userUpdateRequestDTO.getLastName());
        user.setEmail(userUpdateRequestDTO.getEmail());
        user.setBirthDate(userUpdateRequestDTO.getBirthDate());

        return user;
    }
}
