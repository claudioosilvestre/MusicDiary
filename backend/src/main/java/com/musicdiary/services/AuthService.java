package com.musicdiary.services;

import com.musicdiary.dtos.RegisterRequestDTO;


public interface AuthService {

    String register(RegisterRequestDTO registerRequestDTO);
}
