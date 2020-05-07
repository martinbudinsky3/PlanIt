package com.example.vavaplanit.Api;

import com.example.vavaplanit.Model.Event;
import com.example.vavaplanit.Model.User;
import com.example.vavaplanit.Database.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired //so it is not needed to use "new UserService"
    private UserService userService;

    @PostMapping
    public ResponseEntity addUser(@RequestBody User user) {
        logger.info("Inserting new User. Username: " + user.getUserName() + "First name: " + user.getFirstName() + ", last name: " + user.getLastName());
        Integer id = userService.add(user);

        if(id != null) {
            logger.info("User successfully inserted with id [" + id + "].");
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        }


        logger.error("Error inserting new user.Username: " + user.getUserName() + "First name: " + user.getFirstName() +
                ", last name: " + user.getLastName() + ". HTTP Status: " + HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @RequestMapping(value = "/{userName}/{userPassword}", method = RequestMethod.GET)
    public ResponseEntity getUserById(@PathVariable("userName") String userName, @PathVariable("userPassword") String userPassword){
        logger.info("Getting user " + userName + " and user password");
        User user =  userService.getUserByUserNameAndUserPassword(userName, userPassword);

        if (user != null){
            logger.info("User " + user.getUserName() + "successfully logged in");
            return new ResponseEntity<>(user, HttpStatus.OK);
        }

        logger.error("Error logging user " + userName + ". HTTP Status: " + HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
