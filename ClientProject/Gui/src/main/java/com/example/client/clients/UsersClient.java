package com.example.client.clients;

import com.example.client.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


/**
 * Class communicating with server. This class is focused on posting and getting requests related to the User object.
 */

public class UsersClient {
    private final String BASE_URI = "http://localhost:8080";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    private static final Logger logger = LoggerFactory.getLogger(UsersClient.class);

    /**
     * Method needed for login of user. After entering the username and password, this method returns User with given data
     * (if the data is correct and the user was previously registered)
     *
     * @param userName username of given user
     * @param userPassword password of given user
     * @return User object to which the data belong.
     */
    public User getUserByUserNameAndUserPassword(String userName, String userPassword)
            throws JsonProcessingException, ResourceAccessException, HttpStatusCodeException {
        logger.info("Logging user by his username and password: [" + userName + "]");
        final String uri = BASE_URI + "/users/{userName}/{userPassword}";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userName", userName);
        params.put("userPassword", userPassword);

        User user;

        String userJSon = restTemplate.getForObject(uri, String.class, params);
        objectMapper.registerModule(new JavaTimeModule());
        user = objectMapper.readValue(userJSon, new TypeReference<User>() {});
        logger.info("User " + user.getUserName() + "successfully logged in");

        return user;
    }

    /**
     * Method needed for registration of new user. After entering the first name, last name, username and password.
     *
     * @param user User that is going to be registered,
     * @return ID of newly registered user.
     */
    public Integer addUser(User user)
            throws JsonProcessingException, HttpStatusCodeException {
        logger.info("Inserting new User. Username: " + user.getUserName() + " First name: " + user.getFirstName() +
                ", last name: " + user.getLastName());

        final String uri = BASE_URI + "/users";

        RestTemplate restTemplate = new RestTemplate();
        Integer idUser = -1;

        String id = restTemplate.postForObject(uri, user, String.class);
        idUser = objectMapper.readValue(id, Integer.class);
        logger.info("User " + user.getUserName() + " successfully inserted");

        return idUser;
    }
}
