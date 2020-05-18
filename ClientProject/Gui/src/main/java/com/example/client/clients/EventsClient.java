package com.example.client.clients;

import com.example.client.model.Event;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/** Class communicating with server. This class is focused on posting and getting requests related to the Event object. */
public class EventsClient {

    private final ObjectMapper objectMapper = new ObjectMapper();
    static final Logger logger = LoggerFactory.getLogger(EventsClient.class);


    /** Needed to fill the calendar by events.
     * @param userId logged in user,
     * @param month chosen month.
     * @param year cosen year
     * @return List of objects Event.  Method that returns all events for a given month. (Only events belonging to the logged in user) */
    public List<Event> getUserEventsByMonth(int userId, int year, int month) throws Exception {
        logger.info("Getting all user's [" + userId + "] events in year and month: [" + year + ", " + month + "]");

        final String uri = "http://localhost:8080/events/{userId}/{year}/{month}";
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("userId", userId);
        params.put("year", year);
        params.put("month", month);

        List<Event> events = new ArrayList<Event>();

        RestTemplate restTemplate = new RestTemplate();
        try {
            String eventListJSon = restTemplate.getForObject(uri, String.class, params);
            objectMapper.registerModule(new JavaTimeModule());
            events = objectMapper.readValue(eventListJSon, new TypeReference<List<Event>>() {
            });
            logger.info("Returning " + events.size() + " user's [" + userId + "] events in year and month: [" + year + ", " + month + "]");
        }
        catch(ResourceAccessException ex){
            logger.error("Error while connecting to server");
            return null;
        }
        catch (HttpStatusCodeException e){
            if(e.getRawStatusCode() == 500) {
                logger.info("Returning 0 user's [" + userId + "] events in year and month: [" + year + ", " + month + "]");
            } else {
                logger.error("Error. Something went wrong while finding user's [" + userId + "] events in year and month: [" + year + ", " + month + "]. HTTP status: " + e.getRawStatusCode());
                return null;
            }
        }

        return events;
    }

    /** Needed when user wants to see a detail of one of his event.
    * @param idUser user's ID,
     * @param idEvent event's ID.
     * @return chosen Event object. */
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
        catch(ResourceAccessException ex){
            logger.error("Error while connecting to server");
        }
        catch (HttpStatusCodeException e) {
            logger.error("Error. Something went wrong while finding event by user's [" + idUser + "] and event's [" + idEvent + "] ID. HTTP status: " + e.getRawStatusCode());
        }

        return event;
    }

    public List<Event> getEventsToAlert(int idUser) throws Exception{
        logger.info("Getting all user's [" + idUser +"] events to alert.");
        final String uri = "http://localhost:8080/events/alert/{idUser}";
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("idUser", idUser);

        List<Event> events = new ArrayList<Event>();;

        RestTemplate restTemplate = new RestTemplate();
        try {
            String eventsJSon = restTemplate.getForObject(uri, String.class, params);
            objectMapper.registerModule(new JavaTimeModule());
            events = objectMapper.readValue(eventsJSon, new TypeReference<List<Event>>() {});
            logger.info("Returning all user's [" + idUser +"] events to alert.");
        } catch(ResourceAccessException ex){
            logger.error("Error while connecting to server");
        } catch(final HttpStatusCodeException e){
            if(e.getRawStatusCode() == 500) {
                logger.info("Returning 0 events to alert.");
            } else {
                logger.error("Error. Something went wrong while finding all user's [" + idUser +"] events to alert. HTTP status: " + e.getRawStatusCode());
            }
        }
        return events;
    }

    /** Method used to mediate insertion of new event into the database
    * @param event object Event that should be inserted in to calendar.
    * @return ID (integer) of the inserted event. */
    public Integer addEvent(Event event) throws Exception{
        logger.info("Inserting event " + event.getTitle());
        final String uri = "http://localhost:8080/events";
        RestTemplate restTemplate = new RestTemplate();
        Integer idEvent = null;

        try {
            String id = restTemplate.postForObject(uri, event, String.class);
            idEvent = objectMapper.readValue(id, Integer.class);
            logger.info("Event " + event.getTitle() + " successfully inserted");
        } catch(ResourceAccessException ex){
            logger.error("Error while connecting to server");
        } catch(final HttpStatusCodeException e){
            logger.error("Error while inserting event." + event.getTitle() + " HTTP status: " + e.getRawStatusCode());
        }
        return idEvent;

    }


    /** Method needed when user wants to change some data in given event.
    * @param event Event object which is going to be updated,
     *@param id id of that event*/
    public boolean updateEvent(Event event, int id) {
        logger.info("Updating event [" + id + "]");
        final String uri = "http://localhost:8080/events/{idEvent}";
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("idEvent", id);
        RestTemplate restTemplate = new RestTemplate();

        boolean success = false;
        try {
            restTemplate.put(uri, event, params);
            success = true;
            logger.info("Event [" + id + "] successffully updated.");
        } catch(ResourceAccessException ex){
            logger.error("Error while connecting to server");
        } catch (HttpStatusCodeException e){
            logger.error("Error while updating event." + event.getIdEvent() + " HTTP status: " + e.getRawStatusCode());
        }

        return success;
    }

    /** Method used to mediate the deletion of given event.
    * @param idUser ID of the user to whom the event belongs,
     *@param idEvent EID of event that is going to be deleted. */
    public boolean deleteEvent(int idUser, int idEvent) {
        logger.info("Deleting event [" + idEvent + "]");
        final String uri = "http://localhost:8080/events/{idUser}/{idEvent}";
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("idUser", idUser);
        params.put("idEvent", idEvent);
        RestTemplate restTemplate = new RestTemplate();

        boolean success = false;
        try {
            restTemplate.delete(uri, params);
            success = true;
            logger.info("Event [" + idEvent + "] successsffully deleted");
        } catch(ResourceAccessException ex){
            logger.error("Error while connecting to server");
        } catch (HttpStatusCodeException e) {
            logger.error("Error while deleting event." + idEvent + " HTTP status: " + e.getRawStatusCode());
        }

        return success;
    }
}