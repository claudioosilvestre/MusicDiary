package com.musicdiary.services;

import com.musicdiary.dtos.UserUpdateRequestDTO;
import com.musicdiary.dtos.UserUpdateResponseDTO;

public interface UserService {

    UserUpdateResponseDTO getUser(String email);

    UserUpdateResponseDTO updateProfile(String email, UserUpdateRequestDTO userUpdateRequestDTO);

    void deleteAccount(String email);
}
