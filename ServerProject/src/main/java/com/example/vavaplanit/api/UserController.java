package com.example.vavaplanit.api;

import com.example.vavaplanit.dto.event.EventItemDTO;
import com.example.vavaplanit.dto.mappers.EventDTOmapper;
import com.example.vavaplanit.dto.mappers.UserDTOmapper;
import com.example.vavaplanit.dto.user.UserDataDTO;
import com.example.vavaplanit.model.Event;
import com.example.vavaplanit.model.User;
import com.example.vavaplanit.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private UserDTOmapper userDTOmapper;

    @GetMapping
    public ResponseEntity getUser(Principal principal) {
        String username = principal.getName();
        logger.info("Getting info of user {}", username);
        User user = userService.getUserByUsername(username);
        logger.info("User data of user {} successfully found", user);
        UserDataDTO userDataDTO = userDTOmapper.userToUserDataDTO(user);

        return new ResponseEntity<>(userDataDTO, HttpStatus.OK);
    }
}
