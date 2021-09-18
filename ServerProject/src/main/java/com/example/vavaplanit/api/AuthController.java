package com.example.vavaplanit.api;

import com.example.vavaplanit.model.User;
import com.example.vavaplanit.model.dto.user.UserCreateDTO;
import com.example.vavaplanit.model.dto.mappers.UserDTOmapper;
import com.example.vavaplanit.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private UserDTOmapper userDTOmapper;

    /**
     * Inserting new user
     * @param userCreateDTO new user
     * @return id of new user*/
    @PostMapping("register")
    public ResponseEntity register(@RequestBody UserCreateDTO userCreateDTO) {
        logger.info("Registering new user. Username: " + userCreateDTO.getUsername() + ", first name: " + userCreateDTO.getFirstName() + ", last name: " + userCreateDTO.getLastName());

        User userFromDB = userService.getUserByUsername(userCreateDTO.getUsername());
        if(userFromDB != null) {
            logger.info("User with username " + userCreateDTO.getUsername() + " already exists.");

            return new ResponseEntity<>(null, HttpStatus.PRECONDITION_FAILED);
        }

        User user = userDTOmapper.userCreateDTOToUser(userCreateDTO);
        Long id = userService.add(user);
        if(id != null) {
            logger.info("User successfully inserted with id " + id);

            return new ResponseEntity<>(id, HttpStatus.CREATED);
        }

        logger.error("Error registering new user. Username: " + userCreateDTO.getUsername() + "First name: " + userCreateDTO.getFirstName() +
                ", last name: " + userCreateDTO.getLastName() + ". HTTP Status: " + HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
