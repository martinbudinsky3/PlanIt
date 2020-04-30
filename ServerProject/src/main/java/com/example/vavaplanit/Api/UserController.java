package com.example.vavaplanit.Api;

import com.example.vavaplanit.Model.Event;
import com.example.vavaplanit.Model.User;
import com.example.vavaplanit.Database.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired //so it is not needed to use "new UserService"
    private UserService userService;

    @PostMapping
    public ResponseEntity addUser(@RequestBody User user) {
        Integer id = userService.add(user);
        if(id != null) {
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @RequestMapping(value = "/{userName}/{userPassword}", method = RequestMethod.GET)
    public ResponseEntity getUserById(@PathVariable("userName") String userName, @PathVariable("userPassword") String userPassword){
        User user =  userService.getUserByUserNameAndUserPassword(userName, userPassword);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
