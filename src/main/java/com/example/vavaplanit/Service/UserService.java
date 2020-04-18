package com.example.vavaplanit.Service;

import com.example.vavaplanit.Repository.UserRepository;
import com.example.vavaplanit.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserService {

    @Autowired //so it is not needed to use "new UserDao"
    private UserRepository userRepository;

    public Collection<User> getAllUsers(){
        return this.userRepository.getAllUsers();
    }

    public User getUserById(int idUser){
        return this.userRepository.getUserById(idUser);
    }
}
