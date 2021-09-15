package com.example.vavaplanit.service;

import com.example.vavaplanit.database.repository.UserRepository;
import com.example.vavaplanit.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Inserting new user. Used in registration.
     *
     * @param user User object which is going to be inserted
     */
    public Long add(User user) {
        String plainPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(plainPassword));

        return userRepository.add(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }
}
