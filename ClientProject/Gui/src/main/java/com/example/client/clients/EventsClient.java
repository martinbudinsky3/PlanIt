package com.example.client.clients;

import com.example.client.model.Event;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
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

        List<Event> events = null;

        RestTemplate restTemplate = new RestTemplate();
        try {
            String eventListJSon = restTemplate.getForObject(uri, String.class, params);
            objectMapper.registerModule(new JavaTimeModule());
            events = objectMapper.readValue(eventListJSon, new TypeReference<List<Event>>() {
            });
            logger.info("Returning " + events.size() + " user's [" + userId + "] events in year and month: [" + year + ", " + month + "]");
        }
        catch (HttpStatusCodeException e){
            logger.error("Error. Something went wrong while finding user's [" + userId + "] events in year and month: [" + year + ", " + month + "]. HTTP status: " + e.getRawStatusCode());
        }

        return events;
    }

    public Event getEvent(int idUser, int idEvent) throws Exception{
        logger.info("Getting event by user's [" + idUser + "] and event's [" + idEvent + "] ID");
        final String uri = "http://localhost:8080/events/{idUser}/{idEvent}";
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("idUser", idUser);
        params.put("idEvent", idEvent);

        Event event = null;
        RestTemplate restTemplate = new RestTemplate();

        try {
            String eventJSon = restTemplate.getForObject(uri, String.class, params);
            objectMapper.registerModule(new JavaTimeModule());
            event = objectMapper.readValue(eventJSon, new TypeReference<Event>() {});
            logger.info("Returning event by user's [" + idUser + "] and event's [" + idEvent + "] ID");
        }
        catch (HttpStatusCodeException e) {
            logger.error("Error. Something went wrong while finding event by user's [" + idUser + "] and event's [" + idEvent + "] ID. HTTP status: " + e.getRawStatusCode());
        }

        return event;
    }

    public List<Event> getEventToAlert(int idUser) throws Exception{
        logger.info("Getting all user's [" + idUser +"] events to alert.");
        final String uri = "http://localhost:8080/events/alert/{idUser}";
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("idUser", idUser);

        List<Event> events = null;

        RestTemplate restTemplate = new RestTemplate();
        try {
            String eventsJSon = restTemplate.getForObject(uri, String.class, params);
            objectMapper.registerModule(new JavaTimeModule());
            events = objectMapper.readValue(eventsJSon, new TypeReference<List<Event>>() {});
            logger.info("Returning all user's [" + idUser +"] events to alert.");
        } catch(final HttpStatusCodeException e){
            logger.error("Error. Something went wrong while finding all user's [" + idUser +"] events to alert. HTTP status: " + e.getRawStatusCode());
        }
        return events;
    }

    public Integer addEvent(Event event) throws Exception{
        logger.info("Inserting event " + event.getTitle());
        final String uri = "http://localhost:8080/events";
        RestTemplate restTemplate = new RestTemplate();
        Integer idEvent = null;

        try {
            String id = restTemplate.postForObject(uri, event, String.class);
            idEvent = objectMapper.readValue(id, Integer.class);
            logger.info("Event " + event.getTitle() + " successfully inserted");

        } catch(final HttpStatusCodeException e){
            logger.error("Error while inserting event." + event.getTitle() + " HTTP status: " + e.getRawStatusCode());
        }
        return idEvent;

    }

    public void updateEvent(Event event, int id) throws Exception{
        logger.info("Updating event [" + id + "]");
        final String uri = "http://localhost:8080/events/{idEvent}";
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("idEvent", id);
        RestTemplate restTemplate = new RestTemplate();

        try {
            restTemplate.put(uri, event, params);
            logger.info("Event [" + id + "] successffully updated.");
        }
        catch (HttpStatusCodeException e){
            logger.error("Error while updating event." + event.getIdEvent() + " HTTP status: " + e.getRawStatusCode());
        }
    }

    public void deleteEvent(int idUser, int idEvent) throws Exception{
        logger.info("Deleting event [" + idEvent + "]");
        final String uri = "http://localhost:8080/events/{idUser}/{idEvent}";
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("idUser", idUser);
        params.put("idEvent", idEvent);
        RestTemplate restTemplate = new RestTemplate();

        try {
            restTemplate.delete(uri, params);
            logger.info("Event [" + idEvent + "] successsffully deleted");
        }
        catch (HttpStatusCodeException e) {
            logger.error("Error while deleting event." + idEvent + " HTTP status: " + e.getRawStatusCode());
        }
    }
}