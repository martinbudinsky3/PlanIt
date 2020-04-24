package com.example.vavaplanit.Api;

import com.example.vavaplanit.Model.Event;
import com.example.vavaplanit.Model.User;
import com.example.vavaplanit.Database.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/{idUser}", method = RequestMethod.GET)
    public ResponseEntity getUserById(@PathVariable("idUser") int idUser){
        List<User> userList = userService.getUserById(idUser);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }
}
