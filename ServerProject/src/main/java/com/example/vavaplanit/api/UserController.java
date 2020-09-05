package com.example.vavaplanit.api;

import com.example.vavaplanit.model.User;
import com.example.vavaplanit.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired //so it is not needed to use "new UserService"
    private UserService userService;

    /**
     * Inserting new user
     * @param user new user
     * @return id of new user*/
    @PostMapping
    public ResponseEntity addUser(@RequestBody User user) {
        logger.info("Inserting new User. Username: " + user.getUserName() + "First name: " + user.getFirstName() + ", last name: " + user.getLastName());
        User userFromDB = userService.getUserByUsername(user.getUserName());

        if(userFromDB != null) {
            logger.info("User with username [" + user.getUserName() + "] already exists.");

            return new ResponseEntity<>(null, HttpStatus.PRECONDITION_FAILED);
        }

        Integer id = userService.add(user);
        if(id != null) {
            logger.info("User successfully inserted with id [" + id + "].");

            return new ResponseEntity<>(id, HttpStatus.CREATED);
        }

        logger.error("Error inserting new user. Username: " + user.getUserName() + "First name: " + user.getFirstName() +
                ", last name: " + user.getLastName() + ". HTTP Status: " + HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * @param userName username
     * @param userPassword password
     * @return user object to which entered data belong
     */
    @GetMapping(value = "/{userName}/{userPassword}")
    public ResponseEntity getUserByUsernameAndPassword(@PathVariable("userName") String userName, @PathVariable("userPassword") String userPassword){
        logger.info("Getting user " + userName + " and user password");
        User user =  userService.getUserByUsernameAndPassword(userName, userPassword);

        if (user != null){
            logger.info("User " + user.getUserName() + "successfully logged in");
            return new ResponseEntity<>(user, HttpStatus.OK);
        }

        logger.info("User with username[" + userName + "] and given password doesn't exists");
        return new ResponseEntity<>(null, HttpStatus.PRECONDITION_FAILED);
    }
}
