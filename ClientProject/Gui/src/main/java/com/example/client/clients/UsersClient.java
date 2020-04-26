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

    public User getUserById(int idUser) throws Exception{
        final String uri = "http://localhost:8080/users/{idUser}";
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("idUser", idUser);

        RestTemplate restTemplate = new RestTemplate();
        String userJSon = restTemplate.getForObject(uri, String.class, params);
        objectMapper.registerModule(new JavaTimeModule());
        User user = objectMapper.readValue(userJSon, new TypeReference<User>(){});

        return user;
    }
}
