package com.example.client.clients;

import com.example.client.model.Event;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class EventsClient {
    @Autowired
    RestTemplate restTemplate;
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

    public Event getEvent(int idUser, int idEvent) throws Exception{
        final String uri = "http://localhost:8080/events/{idUser}/{idEvent}";
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("idUser", idUser);
        params.put("idEvent", idEvent);

        RestTemplate restTemplate = new RestTemplate();
        String eventJSon = restTemplate.getForObject(uri, String.class, params);
        objectMapper.registerModule(new JavaTimeModule());
        Event event = objectMapper.readValue(eventJSon, new TypeReference<Event>(){});
        return event;
    }

    public Event getEventToAlert(int idUser) throws Exception{
        final String uri = "http://localhost:8080/events/alert/{idUser}";
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("idUser", idUser);

        RestTemplate restTemplate = new RestTemplate();
        try {
            String eventJSon = restTemplate.getForObject(uri, String.class, params);
            objectMapper.registerModule(new JavaTimeModule());
            Event event = objectMapper.readValue(eventJSon, new TypeReference<Event>() {
            });
            return event;
        } catch(final HttpServerErrorException.InternalServerError e){
            return null;
        }
    }

    public int addEvent(Event event) throws Exception{
        final String uri = "http://localhost:8080/events";
        RestTemplate restTemplate = new RestTemplate();
        String id = restTemplate.postForObject(uri, event, String.class);
        Integer idEvent = objectMapper.readValue(id, Integer.class);

        return idEvent;
    }

    public void updateEvent(Event event, int id) throws Exception{
        final String uri = "http://localhost:8080/events/{idEvent}";
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("idEvent", id);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(uri, event, params);
    }

    public void deleteEvent(int idUser, int idEvent) throws Exception{
        final String uri = "http://localhost:8080/events/{idUser}/{idEvent}";
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("idUser", idUser);
        params.put("idEvent", idEvent);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(uri, params);
    }

}
