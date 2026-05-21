package com.musicdiary.services;

import com.musicdiary.dtos.RegisterRequestDTO;
import com.musicdiary.exceptions.UserEmailAlreadyExistsException;
import com.musicdiary.models.User;
import com.musicdiary.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public String register(RegisterRequestDTO registerRequestDTO) {
        if(userRepository.findByEmail(registerRequestDTO.getEmail()).isPresent()) {
            throw new UserEmailAlreadyExistsException();
        }

        String password = passwordEncoder.encode(registerRequestDTO.getPassword());

        User user = new User();
        user.setFirstName(registerRequestDTO.getFirstName());
        user.setLastName(registerRequestDTO.getLastName());
        user.setBirthDate(registerRequestDTO.getBirthDate());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPasswordHash(password);

        userRepository.save(user);

        return null;
    }


}
