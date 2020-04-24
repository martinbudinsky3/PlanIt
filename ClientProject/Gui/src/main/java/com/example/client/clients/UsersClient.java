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

    public List<User> getUserById(int idUser) throws Exception{
        final String uri = "http://localhost:8080/users/{idUser}";
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("idUser", idUser);

        RestTemplate restTemplate = new RestTemplate();
        String userListJSon = restTemplate.getForObject(uri, String.class, params);
        objectMapper.registerModule(new JavaTimeModule());
        List<User> users = objectMapper.readValue(userListJSon, new TypeReference<List<User>>(){});

        return users;
    }
}
