package com.example.vavaplanit.Service;

import com.example.vavaplanit.Database.Repository.UserRepository;
import com.example.vavaplanit.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired //so it is not needed to use "new UserDao"
    private UserRepository userRepository;

    /**
     * Inserting new user. Used in registration.
     * @param  user User object which is going to be inserted */
    public Integer add(User user) {
        Integer idUser = userRepository.add(user);
        return idUser;
    }

    /**
     * Used to login
     * @param userName username of user
     * @param userPassword password of user */
    public User getUserByUserNameAndUserPassword(String userName, String userPassword){
        return this.userRepository.getUserByUserNameAndUserPassword(userName, userPassword);
    }
}
