package com.example.vavaplanit.Service;

import com.example.vavaplanit.Database.Repository.UserRepository;
import com.example.vavaplanit.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired //so it is not needed to use "new UserDao"
    private UserRepository userRepository;

    /**
     * Inserting new user. Used in registration.
     * @param  user User object which is going to be inserted */
    public Integer add(User user) {
        String plainPassword = user.getUserPassword();
        user.setUserPassword(BCrypt.hashpw(plainPassword, BCrypt.gensalt(10)));

        return userRepository.add(user);
    }

    /**
     * Used to login
     * @param username username of user
     * @param password password of user */
    public User getUserByUsernameAndPassword(String username, String password){
        User user = getUserByUsername(username);
        if(user != null && BCrypt.checkpw(password, user.getUserPassword())) {
            return user;
        }

        return null;
    }

    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }
}
