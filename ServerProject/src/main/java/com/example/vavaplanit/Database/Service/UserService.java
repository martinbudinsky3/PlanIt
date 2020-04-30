package com.example.vavaplanit.Database.Service;

import com.example.vavaplanit.Database.Repository.UserRepository;
import com.example.vavaplanit.Model.Event;
import com.example.vavaplanit.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired //so it is not needed to use "new UserDao"
    private UserRepository userRepository;

    public Integer add(User user) {
        Integer idUser = userRepository.add(user);
        return idUser;
    }

    public List<User> getAllUsers(){
        return this.userRepository.getAllUsers();
    }

    public User getUserByUserNameAndUserPassword(String userName, String userPassword){
        return this.userRepository.getUserByUserNameAndUserPassword(userName, userPassword);
    }
}
