package com.example.client.clients;

import com.example.client.model.Event;
import com.example.client.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersClient {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public User getUserByUserNameAndUserPassword(String userName, String userPassword) throws Exception{
        final String uri = "http://localhost:8080/users/{userName}/{userPassword}";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userName", userName);
        params.put("userPassword", userPassword);

        RestTemplate restTemplate = new RestTemplate();
        String userJSon = restTemplate.getForObject(uri, String.class, params);
        objectMapper.registerModule(new JavaTimeModule());
        User user = objectMapper.readValue(userJSon, new TypeReference<User>(){});

        return user;
    }
    public int addUser(User user) throws Exception{
        final String uri = "http://localhost:8080/users";
        RestTemplate restTemplate = new RestTemplate();
        String id = restTemplate.postForObject(uri, user, String.class);
        Integer idUser = objectMapper.readValue(id, Integer.class);

        return idUser;
    }

}
