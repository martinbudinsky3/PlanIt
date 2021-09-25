package com.example.client;

import com.example.model.Event;
import com.example.utils.PropertiesReader;
import com.example.utils.WindowsCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Class communicating with server. This class is focused on posting and getting requests related to the Event object.
 */
public class EventsClient {

    private final PropertiesReader uriPropertiesReader = new PropertiesReader("uri.properties");
    private final String BASE_URI = uriPropertiesReader.getProperty("base-uri");

    private final WindowsCreator windowsCreator = new WindowsCreator();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    static final Logger logger = LoggerFactory.getLogger(EventsClient.class);

    /**
     * Needed to render one calendar field after creation or update of event.
     *
     * @param date date that represents calendar field.
     * @return List of objects Event.  Method that returns all events for a given date. (Only events belonging to the logged in user)
     */
    public List<Event> getEventsByDate(LocalDate date, ResourceBundle resourceBundle) {
        logger.info("Getting all events from date {}", date);

        final String uri = BASE_URI + "/users/profile/events?date={date}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("date", date);

        List<Event> events = new ArrayList<Event>();
        try {
            String eventListJSon = restTemplate.getForObject(uri, String.class, params);
            objectMapper.registerModule(new JavaTimeModule()); // TODO move to constructor
            events = objectMapper.readValue(eventListJSon, new TypeReference<List<Event>>() {
            });
            logger.info("Returning {} events from date {}", events.size(), date);
        } catch (JsonProcessingException | ResourceAccessException | HttpStatusCodeException ex) {
            // TODO improve error handling
            windowsCreator.showErrorAlert(resourceBundle.getString("eventsInMonthErrorMessage"), resourceBundle);
            if (ex instanceof JsonProcessingException) {
                logger.error("Error. Something went wrong with json processing while getting " +
                        "events from date: [" + date + "]", ex);
            } else if (ex instanceof ResourceAccessException) {
                logger.error("Error while connecting to server", ex);
            } else {
                logger.error("Error. Something went wrong while finding events in year and month: ["
                        + date + "]. HTTP status: " + ((HttpStatusCodeException) ex).getRawStatusCode(), ex);
            }
        }

        return events;
    }

    /**
     * Needed to fill the calendar by events.
     *
     * @param month chosen month.
     * @param year  chosen year
     * @return List of objects Event.  Method that returns all events for a given month. (Only events belonging to the logged in user)
     */
    public List<Event> getUserEventsByMonth(int year, int month, ResourceBundle resourceBundle) {
        logger.info("Getting all events in year {} and month {}", year, month);

        final String uri = BASE_URI + "/users/profile/events?year={year}&month={month}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("year", year);
        params.put("month", month);

        List<Event> events = new ArrayList<Event>();

        try {
            String eventListJSon = restTemplate.getForObject(uri, String.class, params);
            objectMapper.registerModule(new JavaTimeModule());
            events = objectMapper.readValue(eventListJSon, new TypeReference<List<Event>>() {
            });
            logger.info("Returning {} events in year {} and month {}", events.size(), year, month);
        } catch (JsonProcessingException | ResourceAccessException | HttpStatusCodeException ex) {
            windowsCreator.showErrorAlert(resourceBundle.getString("eventsInMonthErrorMessage"), resourceBundle);
            if (ex instanceof JsonProcessingException) {
                logger.error("Error. Something went wrong with json processing while finding events in year and month: [" + year + ", " + month + "]", ex);
            } else if (ex instanceof ResourceAccessException) {
                logger.error("Error while connecting to server", ex);
            } else {
                logger.error("Error. Something went wrong while finding events in year and month: ["
                        + year + ", " + month + "]. HTTP status: " + ((HttpStatusCodeException) ex).getRawStatusCode(), ex);
            }
        }

        return events;
    }

    /**
     * Needed when user wants to see a detail of one of his event.
     *
     * @param eventId event's ID.
     * @return chosen Event object.
     */
    public Event getEvent(long eventId, LocalDate date) throws JsonProcessingException, ResourceAccessException,
            HttpStatusCodeException {
        logger.info("Getting event with id {}", eventId);
        final String uri = BASE_URI + "/events/{eventId}?date={date}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("eventId", eventId);
        params.put("date", date);

        String eventJSon = restTemplate.getForObject(uri, String.class, params);
        objectMapper.registerModule(new JavaTimeModule());
        Event event = objectMapper.readValue(eventJSon, new TypeReference<Event>() {
        });
        logger.info("Returning event with id {}", eventId);

        return event;
    }

    /**
     * Needed for alerts.
     *
     * @return list of events that have alert time in current minute
     */
    public List<Event> getEventsToAlert() {
        logger.info("Getting events to alert.");
        final String uri = BASE_URI + "users/profile/events/alert?currentTime={currentTime}";
        Map<String, Object> params = new HashMap<>();
        params.put("currentTime", LocalDateTime.now());

        logger.debug("Current time is:" + LocalDateTime.now());

        List<Event> events = new ArrayList<Event>();
        try {
            String eventsJSon = restTemplate.getForObject(uri, String.class, params);
            objectMapper.registerModule(new JavaTimeModule());
            events = objectMapper.readValue(eventsJSon, new TypeReference<List<Event>>() {
            });
            logger.info("Returning {} events to alert", events.size());
        } catch (JsonProcessingException | ResourceAccessException | HttpStatusCodeException ex) {
            if (ex instanceof JsonProcessingException) {
                logger.error("Error. Something went wrong while finding events to alert", ex);
            } else if (ex instanceof ResourceAccessException) {
                logger.error("Error while connecting to server", ex);
            } else {
                logger.error("Error. Something went wrong while finding events to alert. HTTP status: "
                        + ((HttpStatusCodeException) ex).getRawStatusCode(), ex);
            }
        }

        return events;
    }

    /**
     * Method used to mediate insertion of new event into the database
     *
     * @param event object Event that should be inserted in to calendar.
     * @return ID (integer) of the inserted event.
     */
    public Long addEvent(Event event, ResourceBundle resourceBundle) {
        logger.info("Inserting new event {}", event.getTitle());
        final String uri = BASE_URI + "/events";

        Long eventId = null;
        try {
            String id = restTemplate.postForObject(uri, event, String.class);
            eventId = objectMapper.readValue(id, Long.class);
            logger.info("Event " + event.getTitle() + " successfully inserted");
        } catch (JsonProcessingException | ResourceAccessException | HttpStatusCodeException ex) {
            windowsCreator.showErrorAlert(resourceBundle.getString("addEventErrorMessage"), resourceBundle);
            if (ex instanceof JsonProcessingException) {
                logger.error("Error while inserting event." + event.getTitle(), ex);
            } else if (ex instanceof ResourceAccessException) {
                logger.error("Error while connecting to server", ex);
            } else {
                logger.error("Error while inserting event." + event.getTitle() + " HTTP status: "
                        + ((HttpStatusCodeException) ex).getRawStatusCode(), ex);
            }
        }

        return eventId;
    }


    /**
     * Method needed when user wants to change some data in given event.
     *
     * @param event   Event object with updated attributes,
     * @param eventId id of that event
     */
    public boolean updateEvent(Event event, long eventId, ResourceBundle resourceBundle) {
        logger.info("Updating event with id {}", eventId);
        final String uri = BASE_URI + "/events/{eventId}";
        Map<String, Long> params = new HashMap<>();
        params.put("eventId", eventId);

        boolean success = false;
        try {
            restTemplate.put(uri, event, params);
            success = true;
            logger.info("Event with id {} successfully updated", eventId);
        } catch (ResourceAccessException | HttpStatusCodeException ex) {
            windowsCreator.showErrorAlert(resourceBundle.getString("updateEventErrorMessage"), resourceBundle);
            if (ex instanceof ResourceAccessException) {
                logger.error("Error while connecting to server", ex);
            } else {
                logger.error("Error while updating event." + event.getId() + " HTTP status: "
                        + ((HttpStatusCodeException) ex).getRawStatusCode(), ex);
            }
        }

        return success;
    }

    public boolean updateEventInRepetitionAtDate(Event event, long repetitionId, LocalDate date, ResourceBundle resourceBundle) {
        logger.info("Updating event at date {} in repetition with id {}", date, repetitionId);
        final String uri = BASE_URI + "/repetitions/{repetitionId}/events";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("repetitionId", repetitionId);
        params.put("date", date);

        boolean success = false;

        try {
            restTemplate.put(uri, event, params);
            success = true;
            logger.info("Event at date {} successfully updated in repetition with id {}", date, repetitionId);
        } catch (ResourceAccessException | HttpStatusCodeException ex) {
            windowsCreator.showErrorAlert(resourceBundle.getString("updateEventErrorMessage"), resourceBundle);
            if (ex instanceof ResourceAccessException) {
                logger.error("Error while connecting to server", ex);
            } else {
                logger.error("Error while updating event." + event.getId() + " HTTP status: "
                        + ((HttpStatusCodeException) ex).getRawStatusCode(), ex);
            }
        }

        return success;
    }

    public boolean updateAllEventsInRepetition(Event event, long repetitionId, LocalDate date, ResourceBundle resourceBundle) {
        logger.info("Updating all events in repetition with id {}", repetitionId);
        final String uri = BASE_URI + "/repetitions/{repetitionId}/events";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("repetitionId", repetitionId);

        boolean success = false;

        try {
            restTemplate.put(uri, event, params);
            success = true;
            logger.info("Events in repetition with id {} successfully updated in repetition", repetitionId);
        } catch (ResourceAccessException | HttpStatusCodeException ex) {
            windowsCreator.showErrorAlert(resourceBundle.getString("updateEventErrorMessage"), resourceBundle);
            if (ex instanceof ResourceAccessException) {
                logger.error("Error while connecting to server", ex);
            } else {
                logger.error("Error while updating event." + event.getId() + " HTTP status: "
                        + ((HttpStatusCodeException) ex).getRawStatusCode(), ex);
            }
        }

        return success;
    }

    public boolean updateRepetition(Event event, long repetitionId, ResourceBundle resourceBundle) {
        logger.info("Updating repetition with id {}", repetitionId);
        final String uri = BASE_URI + "/repetitions/{repetitionId}";
        Map<String, Long> params = new HashMap<>();
        params.put("repetitionId", repetitionId);

        boolean success = false;
        try {
            restTemplate.put(uri, event, params);
            success = true;
            logger.info("Repetition with id {} successfully updated", repetitionId);
        } catch (ResourceAccessException | HttpStatusCodeException ex) {
            windowsCreator.showErrorAlert(resourceBundle.getString("updateEventErrorMessage"), resourceBundle);
            if (ex instanceof ResourceAccessException) {
                logger.error("Error while connecting to server", ex);
            } else {
                logger.error("Error while updating repetition." + event.getRepetition().getEventId() + " HTTP status: "
                        + ((HttpStatusCodeException) ex).getRawStatusCode(), ex);
            }
        }

        return success;
    }

    /**
     * Method used to mediate the deletion of given event.
     *
     * @param eventId ID of event that is going to be deleted.
     */
    public boolean deleteEvent(long eventId, ResourceBundle resourceBundle) {
        logger.info("Deleting event with id {}", eventId);
        final String uri = BASE_URI + "/events/{eventId}";
        Map<String, Long> params = new HashMap<String, Long>();
        params.put("eventId", eventId);

        boolean success = false;
        try {
            restTemplate.delete(uri, params);
            success = true;
            logger.info("Event with id {} successfully deleted", eventId);
        } catch (ResourceAccessException | HttpStatusCodeException ex) {
            windowsCreator.showErrorAlert(resourceBundle.getString("deleteEventErrorMessage"), resourceBundle);
            if (ex instanceof ResourceAccessException) {
                logger.error("Error while connecting to server", ex);
            } else {
                logger.error("Error while deleting event." + eventId + " HTTP status: "
                        + ((HttpStatusCodeException) ex).getRawStatusCode(), ex);
            }
        }

        return success;
    }

    public boolean deleteFromRepetition(long repetitionId, LocalDate date, ResourceBundle resourceBundle) {
        logger.info("Deleting event at date {} from repetition with id {}", date, repetitionId);
        final String uri = BASE_URI + "/repetitions/{repetitionId}/events?date={date}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("repetitionId", repetitionId);
        params.put("date", date);

        boolean success = false;
        try {
            restTemplate.delete(uri, params);
            success = true;
            logger.info("Event at date {} successfully deleted from repetition with id {}", date, repetitionId);
        } catch (ResourceAccessException | HttpStatusCodeException ex) {
            windowsCreator.showErrorAlert(resourceBundle.getString("deleteEventErrorMessage"), resourceBundle);
            if (ex instanceof ResourceAccessException) {
                logger.error("Error while connecting to server", ex);
            } else {
                logger.error("Error while deleting event." + repetitionId + " HTTP status: "
                        + ((HttpStatusCodeException) ex).getRawStatusCode(), ex);
            }
        }

        return success;
    }
}