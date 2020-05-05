package com.example.client.clients;

import com.example.client.model.Event;
import com.example.client.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersClient {
    private final ObjectMapper objectMapper = new ObjectMapper();
    static final Logger logger = LoggerFactory.getLogger(UsersClient.class);


    public User getUserByUserNameAndUserPassword(String userName, String userPassword) throws Exception{
        logger.info("Logging user by his username and password: [" + userName + "]");
        final String uri = "http://localhost:8080/users/{userName}/{userPassword}";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userName", userName);
        params.put("userPassword", userPassword);

        RestTemplate restTemplate = new RestTemplate();

        User user = null;

        try {
            String userJSon = restTemplate.getForObject(uri, String.class, params);
            objectMapper.registerModule(new JavaTimeModule());
            user = objectMapper.readValue(userJSon, new TypeReference<User>(){});
            logger.info("User successfully logged in");
        }
        catch (final HttpServerErrorException.InternalServerError e) {
            logger.error("Error logging user.");
        }

        return user;
    }
    public int addUser(User user) throws Exception{
        logger.info("Inserting new user [" + user.getFirstName() + "," + user.getLastName() + "]");
        final String uri = "http://localhost:8080/users";

        RestTemplate restTemplate = new RestTemplate();
        Integer idUser = -1;
        try {
            String id = restTemplate.postForObject(uri, user, String.class);
            idUser = objectMapper.readValue(id, Integer.class);
            logger.info("User successfully inserted");
        }
        catch (final HttpServerErrorException.InternalServerError e) {
            logger.error("Error inserting user.");

        }
        return idUser;
    }

}
