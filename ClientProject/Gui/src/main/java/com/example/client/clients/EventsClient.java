package com.example.client.clients;

import com.example.client.model.Event;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

public class EventsClient {
    private final ObjectMapper objectMapper = new ObjectMapper();

    // main only for testing
    public static void main(String[] args) throws Exception{
        EventsClient eventsClient = new EventsClient();
        eventsClient.getUserEventsByMonth(1, 2020, 5);
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

    public List<Event> getUserEventsByMonth(int userId, int year, int month) throws Exception{
        final String uri = "http://localhost:8080/events/{userId}/{year}/{month}";
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("userId", userId);
        params.put("year", year);
        params.put("month", month);

        RestTemplate restTemplate = new RestTemplate();
        String eventListJSon = restTemplate.getForObject(uri, String.class, params);
        objectMapper.registerModule(new JavaTimeModule());
        List<Event> events = objectMapper.readValue(eventListJSon, new TypeReference<List<Event>>(){});

        return events;
    }
}
