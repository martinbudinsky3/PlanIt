package com.example.vavaplanit.Api;

import com.example.vavaplanit.Model.User;
import com.example.vavaplanit.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired //so it is not needed to use "new UserService"
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

//    @RequestMapping(value = "/{idUser}", method = RequestMethod.GET)
//    public User getUserById(@PathVariable("idUser") int idUser){
//        return userService.getUserById(idUser);
//    }
}
