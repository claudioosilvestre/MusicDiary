package com.musicdiary.controllers;

import com.musicdiary.dtos.UserUpdateRequestDTO;
import com.musicdiary.dtos.UserUpdateResponseDTO;
import com.musicdiary.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping
    public ResponseEntity<UserUpdateResponseDTO> updateProfile(@Valid @RequestBody UserUpdateRequestDTO userUpdateRequestDTO) {

        UserUpdateResponseDTO responseDTO = userService.updateProfile(
                SecurityContextHolder.getContext().getAuthentication().getName(), userUpdateRequestDTO);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser() {


        userService.deleteAccount(SecurityContextHolder.getContext().getAuthentication().getName());

        return ResponseEntity.noContent().build();
    }
}