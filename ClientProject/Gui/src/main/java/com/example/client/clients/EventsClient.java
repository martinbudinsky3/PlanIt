package com.example.client.clients;

import com.example.client.model.Event;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class EventsClient {

    private final ObjectMapper objectMapper = new ObjectMapper();
    static final Logger logger = LoggerFactory.getLogger(EventsClient.class);


    public List<Event> getUserEventsByMonth(int userId, int year, int month) throws Exception{
        logger.info("Getting all user's [" + userId + "] events in year and month: [" + year + ", " + month + "]");
        final String uri = "http://localhost:8080/events/{userId}/{year}/{month}";
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("userId", userId);
        params.put("year", year);
        params.put("month", month);

        RestTemplate restTemplate = new RestTemplate();
        String eventListJSon = restTemplate.getForObject(uri, String.class, params);
        objectMapper.registerModule(new JavaTimeModule());
        List<Event> events = objectMapper.readValue(eventListJSon, new TypeReference<List<Event>>(){});
        logger.info("Returning events by year and month.");

        return events;
    }

    public Event getEvent(int idUser, int idEvent) throws Exception{
        logger.info("Getting event by user's [" + idUser + "] and event's [" + idEvent + "] ID");
        final String uri = "http://localhost:8080/events/{idUser}/{idEvent}";
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("idUser", idUser);
        params.put("idEvent", idEvent);

        RestTemplate restTemplate = new RestTemplate();
        String eventJSon = restTemplate.getForObject(uri, String.class, params);
        objectMapper.registerModule(new JavaTimeModule());
        Event event = objectMapper.readValue(eventJSon, new TypeReference<Event>(){});
        logger.info("Returning event.");
        return event;
    }

    public List<Event> getEventToAlert(int idUser) throws Exception{
        logger.info("Getting all user's [" + idUser +"] events to alert.");
        final String uri = "http://localhost:8080/events/alert/{idUser}";
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("idUser", idUser);

        RestTemplate restTemplate = new RestTemplate();
        try {
            String eventsJSon = restTemplate.getForObject(uri, String.class, params);
            objectMapper.registerModule(new JavaTimeModule());
            List<Event> events = objectMapper.readValue(eventsJSon, new TypeReference<List<Event>>() {
            });
            logger.info("Returning events to alert.");
            return events;
        } catch(final HttpServerErrorException.InternalServerError e){
            logger.error("Server error in returning events.");
            return null;
        }
    }

    public int addEvent(Event event) throws Exception{
        logger.info("Inserting event");
        final String uri = "http://localhost:8080/events";
        RestTemplate restTemplate = new RestTemplate();

        try {
            String id = restTemplate.postForObject(uri, event, String.class);
            Integer idEvent = objectMapper.readValue(id, Integer.class);
            logger.info("Event successfully inserted");
            return idEvent;

        } catch(final HttpServerErrorException.InternalServerError e){
            logger.error("Server error in inserting event.");
            return 0;
        }

    }

    public void updateEvent(Event event, int id) throws Exception{
        logger.info("Updating event [" + id + "]");
        final String uri = "http://localhost:8080/events/{idEvent}";
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("idEvent", id);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(uri, event, params);
        logger.info("Event [" + id + "] successffully updated");
    }

    public void deleteEvent(int idUser, int idEvent) throws Exception{
        logger.info("Deleting event [" + idEvent + "]");
        final String uri = "http://localhost:8080/events/{idUser}/{idEvent}";
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("idUser", idUser);
        params.put("idEvent", idEvent);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(uri, params);
        logger.info("Event [" + idEvent + "] successsffully deleted");
    }

}
