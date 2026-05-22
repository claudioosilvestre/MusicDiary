package com.musicdiary.services;

import com.musicdiary.dtos.LoginRequestDTO;
import com.musicdiary.dtos.RegisterRequestDTO;
import com.musicdiary.exceptions.EmailNotFoundException;
import com.musicdiary.exceptions.PasswordDoesNotMatchException;
import com.musicdiary.exceptions.UserEmailAlreadyExistsException;
import com.musicdiary.models.User;
import com.musicdiary.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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

        return jwtService.generateToken(user);
    }

    @Override
    public String login(LoginRequestDTO loginRequestDTO) {
        if(loginRequestDTO == null) {
            throw new IllegalArgumentException("Login Request cannot be null");
        }

        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new EmailNotFoundException());

        Boolean passwordMatches = passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPasswordHash());

        if(!passwordMatches) {
            throw new PasswordDoesNotMatchException();
        }

        return jwtService.generateToken(user);
    }
}
