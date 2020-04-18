package com.example.vavaplanit.Api;

import com.example.vavaplanit.Model.User;
import com.example.vavaplanit.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class UserController {

    @Autowired //so it is not needed to use "new UserService"
    private UserService userService;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public Collection<User> getAllUsers(){
        return userService.getAllUsers();
    }
}
