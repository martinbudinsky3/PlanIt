package com.example.client.clients;

import com.example.client.model.Event;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventsClient {
    private final ObjectMapper objectMapper = new ObjectMapper();

    // main only for testing
    public static void main(String[] args) throws Exception{
        EventsClient eventsClient = new EventsClient();
        eventsClient.getUserEvents(1);
    }

    public List<Event> getUserEvents(int userId) throws Exception{
        final String uri = "http://localhost:8080/events/{userId}";
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("userId", userId);

        RestTemplate restTemplate = new RestTemplate();
        String eventListJSon = restTemplate.getForObject(uri, String.class, params);
        objectMapper.registerModule(new JavaTimeModule());
        List<Event> events = objectMapper.readValue(eventListJSon, new TypeReference<List<Event>>(){});

        return events;
    }
}
