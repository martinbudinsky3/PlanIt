package com.example.vavaplanit.Service;

import com.example.vavaplanit.Dao.UserDao;
import com.example.vavaplanit.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserService {

    @Autowired //so it is not needed to use "new UserDao"
    private UserDao userDao;

    public Collection<User> getAllUsers(){
        return userDao.getAllUsers();
    }
}
