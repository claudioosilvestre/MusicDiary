package com.musicdiary.services;

import com.musicdiary.exceptions.UserNotFoundException;
import com.musicdiary.models.User;
import com.musicdiary.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
