package com.musicdiary.services;

import com.musicdiary.dtos.LoginRequestDTO;
import com.musicdiary.dtos.RegisterRequestDTO;


public interface AuthService {

    String register(RegisterRequestDTO registerRequestDTO);
    
    String login(LoginRequestDTO loginRequestDTO);
}
