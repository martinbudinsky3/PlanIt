package com.example.vavaplanit.service;

import com.example.vavaplanit.database.repository.UserRepository;
import com.example.vavaplanit.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired //so it is not needed to use "new UserDao"
    private UserRepository userRepository;

    /**
     * Inserting new user. Used in registration.
     *
     * @param user User object which is going to be inserted
     */
    public Integer add(User user) {
        String plainPassword = user.getPassword();
        user.setPassword(BCrypt.hashpw(plainPassword, BCrypt.gensalt(10)));

        return userRepository.add(user);
    }

    /**
     * Used to login
     *
     * @param username username of user
     * @param password password of user
     */
    public User getUserByUsernameAndPassword(String username, String password) {
        User user = getUserByUsername(username);
        if(user == null) {
            return null;
        }

        if (!userRepository.getHashed(username)) {
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10)));
            userRepository.updateHashed(username, user.getPassword());
        }

        if (BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }

        return null;
    }

    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }
}
