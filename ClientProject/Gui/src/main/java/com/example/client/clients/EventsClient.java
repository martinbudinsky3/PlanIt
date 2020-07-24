package com.example.client.clients;

import com.example.client.model.Event;
import com.example.gui.utils.WindowsCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

/** Class communicating with server. This class is focused on posting and getting requests related to the Event object. */
public class EventsClient {

    private final String BASE_URI = "http://localhost:8080";

    private final WindowsCreator windowsCreator = new WindowsCreator();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    static final Logger logger = LoggerFactory.getLogger(EventsClient.class);


    /** Needed to fill the calendar by events.
     * @param userId logged in user,
     * @param month chosen month.
     * @param year chosen year
     * @return List of objects Event.  Method that returns all events for a given month. (Only events belonging to the logged in user) */
    public List<Event> getUserEventsByMonth(int userId, int year, int month, ResourceBundle resourceBundle) {
        logger.info("Getting all user's [" + userId + "] events in year and month: [" + year + ", " + month + "]");

        final String uri = BASE_URI + "/events/{userId}/{year}/{month}";
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("userId", userId);
        params.put("year", year);
        params.put("month", month);

        List<Event> events = new ArrayList<Event>();

        try {
            String eventListJSon = restTemplate.getForObject(uri, String.class, params);
            objectMapper.registerModule(new JavaTimeModule());
            events = objectMapper.readValue(eventListJSon, new TypeReference<List<Event>>() {});
            logger.info("Returning " + events.size() + " user's [" + userId + "] events in year and month: [" + year + ", " + month + "]");
        } catch (JsonProcessingException | ResourceAccessException | HttpStatusCodeException ex) {
            windowsCreator.showErrorAlert(resourceBundle.getString("eventsInMonthErrorMessage"), resourceBundle);
            if(ex instanceof JsonProcessingException) {
                logger.error("Error. Something went wrong while finding user's [" + userId + "] events in year and month: [" + year + ", " + month + "]", ex);
            } else if(ex instanceof ResourceAccessException) {
                logger.error("Error while connecting to server", ex);
            } else {
                if(((HttpStatusCodeException)ex).getRawStatusCode() == 500) {
                    logger.info("Returning 0 user's [" + userId + "] events in year and month: [" + year + ", " + month + "]");
                } else {
                    logger.error("Error. Something went wrong while finding user's [" + userId + "] events in year and month: ["
                            + year + ", " + month + "]. HTTP status: " + ((HttpStatusCodeException)ex).getRawStatusCode(), ex);
                }
            }
        }

        return events;
    }

    /** Needed when user wants to see a detail of one of his event.
    * @param idUser user's ID,
     * @param idEvent event's ID.
     * @return chosen Event object. */
    public Event getEvent(int idUser, int idEvent, ResourceBundle resourceBundle) {
        logger.info("Getting event by user's [" + idUser + "] and event's [" + idEvent + "] ID");
        final String uri = BASE_URI + "/events/{idUser}/{idEvent}";
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("idUser", idUser);
        params.put("idEvent", idEvent);

        Event event = null;

        try {
            String eventJSon = restTemplate.getForObject(uri, String.class, params);
            objectMapper.registerModule(new JavaTimeModule());
            event = objectMapper.readValue(eventJSon, new TypeReference<Event>() {});
            logger.info("Returning event by user's [" + idUser + "] and event's [" + idEvent + "] ID");
        } catch (JsonProcessingException | ResourceAccessException | HttpStatusCodeException ex) {
            windowsCreator.showErrorAlert(resourceBundle.getString("getEventErrorMessage"), resourceBundle);
            if(ex instanceof JsonProcessingException) {
                logger.error("Error. Something went wrong while finding event by user's [" + idUser + "] and event's [" + idEvent + "] ID", ex);
            } else if(ex instanceof ResourceAccessException) {
                logger.error("Error while connecting to server", ex);
            } else {
                logger.error("Error. Something went wrong while finding event by user's [" + idUser + "] and event's [" + idEvent + "] ID." +
                        " HTTP status: " + ((HttpStatusCodeException) ex).getRawStatusCode(), ex);
            }
        }

        return event;
    }

    /**
     * Needed for alerts.
     * @param idUser user's ID,
     * @return list of events that have alert time in current minute
    */
    public List<Event> getEventsToAlert(int idUser, ResourceBundle resourceBundle){
        logger.info("Getting all user's [" + idUser +"] events to alert.");
        final String uri = BASE_URI + "/events/alert/{idUser}/{currentTime}";
        Map<String, Object> params = new HashMap<>();
        params.put("idUser", idUser);
        params.put("currentTime", LocalDateTime.now());

        logger.debug("Current time is:" + LocalDateTime.now());

        List<Event> events = new ArrayList<Event>();;

        try {
            String eventsJSon = restTemplate.getForObject(uri, String.class, params);
            objectMapper.registerModule(new JavaTimeModule());
            events = objectMapper.readValue(eventsJSon, new TypeReference<List<Event>>() {});
            logger.info("Returning all user's [" + idUser +"] events to alert.");
        } catch (JsonProcessingException | ResourceAccessException | HttpStatusCodeException ex) {
            if(ex instanceof JsonProcessingException) {
                logger.error("Error. Something went wrong while finding all user's [" + idUser +"] events to alert", ex);
            } else if(ex instanceof ResourceAccessException) {
                logger.error("Error while connecting to server", ex);
            } else {
                if(((HttpStatusCodeException) ex).getRawStatusCode() == 500) {
                    logger.info("Returning 0 events to alert.");
                } else {
                    logger.error("Error. Something went wrong while finding all user's [" + idUser +"] events to alert. HTTP status: "
                            + ((HttpStatusCodeException) ex).getRawStatusCode(), ex);
                }
            }
        }

        return events;
    }

    /** Method used to mediate insertion of new event into the database
    * @param event object Event that should be inserted in to calendar.
    * @return ID (integer) of the inserted event. */
    public Integer addEvent(Event event, ResourceBundle resourceBundle) {
        logger.info("Inserting event " + event.getTitle());
        final String uri = BASE_URI + "/events";
        Integer idEvent = null;

        try {
            String id = restTemplate.postForObject(uri, event, String.class);
            idEvent = objectMapper.readValue(id, Integer.class);
            logger.info("Event " + event.getTitle() + " successfully inserted");
        } catch (JsonProcessingException | ResourceAccessException | HttpStatusCodeException ex) {
            windowsCreator.showErrorAlert(resourceBundle.getString("addEventErrorMessage"), resourceBundle);
            if(ex instanceof JsonProcessingException) {
                logger.error("Error while inserting event." + event.getTitle(), ex);
            } else if(ex instanceof ResourceAccessException) {
                logger.error("Error while connecting to server", ex);
            } else {
                logger.error("Error while inserting event." + event.getTitle() + " HTTP status: "
                        + ((HttpStatusCodeException) ex).getRawStatusCode(), ex);
            }
        }

        return idEvent;
    }


    /** Method needed when user wants to change some data in given event.
    * @param event Event object with updated attributes,
     *@param id id of that event*/
    public boolean updateEvent(Event event, int id, ResourceBundle resourceBundle) {
        logger.info("Updating event [" + id + "]");
        final String uri = BASE_URI + "/events/{idEvent}";
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("idEvent", id);

        boolean success = false;
        try {
            restTemplate.put(uri, event, params);
            success = true;
            logger.info("Event [" + id + "] successffully updated.");
        } catch (ResourceAccessException | HttpStatusCodeException ex) {
            windowsCreator.showErrorAlert(resourceBundle.getString("updateEventErrorMessage"), resourceBundle);
            if(ex instanceof ResourceAccessException) {
                logger.error("Error while connecting to server", ex);
            } else {
                logger.error("Error while updating event." + event.getIdEvent() + " HTTP status: "
                        + ((HttpStatusCodeException)ex).getRawStatusCode(), ex);
            }
        }

        return success;
    }

    /** Method used to mediate the deletion of given event.
    * @param idUser ID of the user to whom the event belongs,
     *@param idEvent ID of event that is going to be deleted. */
    public boolean deleteEvent(int idUser, int idEvent, ResourceBundle resourceBundle) {
        logger.info("Deleting event [" + idEvent + "]");
        final String uri = BASE_URI + "/events/{idUser}/{idEvent}";
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("idUser", idUser);
        params.put("idEvent", idEvent);

        boolean success = false;
        try {
            restTemplate.delete(uri, params);
            success = true;
            logger.info("Event [" + idEvent + "] successsffully deleted");
        } catch (ResourceAccessException | HttpStatusCodeException ex) {
            windowsCreator.showErrorAlert(resourceBundle.getString("deleteEventErrorMessage"), resourceBundle);
            if(ex instanceof ResourceAccessException) {
                logger.error("Error while connecting to server", ex);
            } else {
                logger.error("Error while deleting event." + idEvent + " HTTP status: "
                        + ((HttpStatusCodeException)ex).getRawStatusCode(), ex);
            }
        }

        return success;
    }
}