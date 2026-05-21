package com.musicdiary.services;

import com.musicdiary.models.User;

public interface JwtService {

    String generateToken(User user);
    
    String extractEmail(String token);
}
