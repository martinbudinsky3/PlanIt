package com.example.client.clients;

import com.example.client.exceptions.AccessDeniedException;
import com.example.client.exceptions.NotFoundException;
import com.example.client.exceptions.UnauthorizedException;
import com.example.client.utils.Utils;
import com.example.dto.event.*;
import com.example.dto.mappers.EventDTOmapper;
import com.example.dto.mappers.RepetitionDTOmapper;
import com.example.dto.repetition.RepetitionCreateDTO;
import com.example.model.Event;
import com.example.model.repetition.Repetition;
import com.example.utils.PropertiesReader;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
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

    private final ObjectMapper objectMapper = new ObjectMapper();
    private EventDTOmapper eventDTOmapper = new EventDTOmapper();
    private RepetitionDTOmapper repetitionDTOmapper = new RepetitionDTOmapper();
    private final RestTemplate restTemplate = new RestTemplate();
    static final Logger logger = LoggerFactory.getLogger(EventsClient.class);
    private final Utils utils = new Utils();


    public EventsClient() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Needed to render one calendar field after creation or update of event.
     *
     * @param date date that represents calendar field.
     * @return List of objects Event.  Method that returns all events for a given date. (Only events belonging to the logged in user)
     */
    public List<Event> getEventsByDate(LocalDate date) throws Exception {
        logger.info("Getting all events from date {}", date);

        final String uri = BASE_URI + "/users/profile/events?date={date}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("date", date);

        List<Event> events;
        try {
            HttpHeaders headers = utils.createAuthenticationHeader();
            HttpEntity entity = new HttpEntity(headers);
            ResponseEntity response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class, params);

            String eventListJSon = (String) response.getBody();
            events = objectMapper.readValue(eventListJSon, new TypeReference<List<Event>>() {
            });
            logger.info("Returning {} events from date {}", events.size(), date);
        } catch (Exception ex) {
            logger.error("Error while getting events from date {}", date, ex);
            if (ex instanceof HttpStatusCodeException) {
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    throw new UnauthorizedException();
                }
            }

            throw ex;
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
    public List<Event> getUserEventsByMonth(int year, int month) throws Exception {
        logger.info("Getting all events in year {} and month {}", year, month);

        final String uri = BASE_URI + "/users/profile/events?year={year}&month={month}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("year", year);
        params.put("month", month);

        List<Event> events;
        try {
            HttpHeaders headers = utils.createAuthenticationHeader();
            HttpEntity entity = new HttpEntity(headers);
            ResponseEntity response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class, params);

            String eventItemsJson = (String) response.getBody();
            List<EventItemDTO> eventItems = objectMapper.readValue(eventItemsJson, new TypeReference<List<EventItemDTO>>() {
            });
            events = eventDTOmapper.eventItemsDTOsToEvents(eventItems);
            logger.info("Returning {} events in year {} and month {}", events.size(), year, month);
        } catch (Exception ex) {
            logger.error("Error  while getting events in year {} and month {}", year, month, ex);
            if (ex instanceof HttpStatusCodeException) {
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    throw new UnauthorizedException();
                }
            }

            throw ex;
        }

        return events;
    }

    /**
     * Needed when user wants to see a detail of one of his event.
     *
     * @param eventId event's ID.
     * @return chosen Event object.
     */
    public Event getEvent(long eventId, LocalDate date) throws Exception {
        logger.info("Getting event with id {}", eventId);
        final String uri = BASE_URI + "/events/{eventId}?date={date}";
        Map<String, Object> params = new HashMap<>();
        params.put("eventId", eventId);
        params.put("date", date);

        Event event;
        try {
            HttpHeaders headers = utils.createAuthenticationHeader();
            HttpEntity entity = new HttpEntity(headers);
            ResponseEntity response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class, params);

            String eventDetailJSon = (String) response.getBody();
            EventDetailDTO eventDetailDTO = objectMapper.readValue(eventDetailJSon, new TypeReference<EventDetailDTO>() {
            });
            event = eventDTOmapper.eventDetailDTOToEvent(eventDetailDTO);
            logger.info("Returning event with id {}", eventId);
        } catch (Exception e) {
            logger.error("Error while getting event with id {}", eventId, e);
            if (e instanceof HttpStatusCodeException) {
                if (((HttpStatusCodeException) e).getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    throw new UnauthorizedException();
                }
                if (((HttpStatusCodeException) e).getStatusCode() == HttpStatus.FORBIDDEN) {
                    throw new AccessDeniedException();
                }
                if (((HttpStatusCodeException) e).getStatusCode() == HttpStatus.NOT_FOUND) {
                    throw new NotFoundException();
                }
            }

            throw e;
        }

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

        List<Event> events = new ArrayList<Event>();
        try {
            HttpHeaders headers = utils.createAuthenticationHeader();
            HttpEntity entity = new HttpEntity(headers);
            ResponseEntity response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class, params);

            String eventAlertDTOsJSon = (String) response.getBody();
            List<EventAlertDTO> eventAlertDTOs = objectMapper.readValue(eventAlertDTOsJSon, new TypeReference<List<EventAlertDTO>>() {
            });
            events = eventDTOmapper.eventAlertDTOsToEvents(eventAlertDTOs);
            logger.info("Returning {} events to alert", events.size());
        } catch (Exception ex) {
            logger.error("Error while getting events to alert", ex);
        }

        return events;
    }

    /**
     * Method used to mediate insertion of new event into the database
     *
     * @param event object Event that should be inserted in to calendar.
     * @return ID (integer) of the inserted event.
     */
    public Long addEvent(Event event) throws Exception {
        logger.info("Inserting new event {}", event.getTitle());
        final String uri = BASE_URI + "/events";

        EventCreateDTO eventCreateDTO = eventDTOmapper.eventToEventCreateDTO(event);
        logger.debug("eventCreateDTO: {}", eventCreateDTO);

        Long eventId;
        try {
            HttpHeaders headers = utils.createAuthenticationHeader();
            HttpEntity entity = new HttpEntity(eventCreateDTO, headers);
            ResponseEntity response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);

            String id = (String) response.getBody();
            eventId = objectMapper.readValue(id, Long.class);
            logger.info("Event '{}' successfully inserted", event.getTitle());
        } catch (Exception ex) {
            logger.error("Error while inserting new event '{}'", event.getTitle(), ex);
            if (ex instanceof HttpStatusCodeException) {
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    throw new UnauthorizedException();
                }
            }

            throw ex;
        }

        return eventId;
    }


    /**
     * Method needed when user wants to change some data in given event.
     *
     * @param event   Event object with updated attributes,
     * @param eventId id of that event
     */
    public void updateEvent(Event event, long eventId) throws Exception {
        logger.info("Updating event with id {}", eventId);
        final String uri = BASE_URI + "/events/{eventId}";
        Map<String, Long> params = new HashMap<>();
        params.put("eventId", eventId);

        EventUpdateDTO eventUpdateDTO = eventDTOmapper.eventToEventUpdateDTO(event);
        try {
            HttpHeaders headers = utils.createAuthenticationHeader();
            HttpEntity entity = new HttpEntity(eventUpdateDTO, headers);
            restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class, params);

            logger.info("Event with id {} successfully updated", eventId);
        } catch (Exception ex) {
            logger.error("Error while updating event with id {}", eventId, ex);
            if (ex instanceof HttpStatusCodeException) {
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    throw new UnauthorizedException();
                }
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.FORBIDDEN) {
                    throw new AccessDeniedException();
                }
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.NOT_FOUND) {
                    throw new NotFoundException();
                }
            }

            throw ex;
        }
    }

    public void postponeEvent(Event event, long eventId) throws Exception {
        logger.info("Postponing event with id {}", eventId);
        final String uri = BASE_URI + "/events/{eventId}/alert";
        Map<String, Long> params = new HashMap<>();
        params.put("eventId", eventId);

        EventPostponeDTO eventPostponeDTO = eventDTOmapper.eventToEventPostponeDTO(event);
        try {
            HttpHeaders headers = utils.createAuthenticationHeader();
            HttpEntity entity = new HttpEntity(eventPostponeDTO, headers);
            restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class, params);

            logger.info("Event with id {} successfully postponed", eventId);
        } catch (Exception ex) {
            logger.error("Error while postponing event with id {}", eventId, ex);
            if (ex instanceof HttpStatusCodeException) {
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    throw new UnauthorizedException();
                }
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.FORBIDDEN) {
                    throw new AccessDeniedException();
                }
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.NOT_FOUND) {
                    throw new NotFoundException();
                }
            }

            throw ex;
        }
    }

    public void updateEventInRepetitionAtDate(Event event, long repetitionId, LocalDate date)
            throws Exception {
        logger.info("Updating event at date {} in repetition with id {}", date, repetitionId);
        final String uri = BASE_URI + "/repetitions/{repetitionId}/events?date={date}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("repetitionId", repetitionId);
        params.put("date", date);

        EventUpdateDTO eventUpdateDTO = eventDTOmapper.eventToEventUpdateDTO(event);
        try {
            HttpHeaders headers = utils.createAuthenticationHeader();
            HttpEntity entity = new HttpEntity(eventUpdateDTO, headers);
            restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class, params);

            logger.info("Event at date {} successfully updated in repetition with id {}", date, repetitionId);
        } catch (Exception ex) {
            logger.error("Error while updating event at date {} in repetition with id {}", date, repetitionId, ex);
            if (ex instanceof HttpStatusCodeException) {
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    throw new UnauthorizedException();
                }
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.FORBIDDEN) {
                    throw new AccessDeniedException();
                }
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.NOT_FOUND) {
                    throw new NotFoundException();
                }
            }

            throw ex;
        }
    }

    public void updateAllEventsInRepetition(Event event, long repetitionId)
            throws Exception {

        logger.info("Updating all events in repetition with id {}", repetitionId);
        final String uri = BASE_URI + "/repetitions/{repetitionId}/events";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("repetitionId", repetitionId);

        RepeatedEventUpdateDTO repeatedEventUpdateDTO = eventDTOmapper.eventToRepeatedEventUpdateDTO(event);
        try {
            HttpHeaders headers = utils.createAuthenticationHeader();
            HttpEntity entity = new HttpEntity(repeatedEventUpdateDTO, headers);
            restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class, params);

            logger.info("Events in repetition with id {} successfully updated in repetition", repetitionId);
        } catch (Exception ex) {
            logger.error("Error while updating all events in repetition with id {}", repetitionId, ex);
            if (ex instanceof HttpStatusCodeException) {
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    throw new UnauthorizedException();
                }
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.FORBIDDEN) {
                    throw new AccessDeniedException();
                }
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.NOT_FOUND) {
                    throw new NotFoundException();
                }
            }

            throw ex;
        }
    }

    public Long addRepetition(Long eventId, Repetition repetition) throws Exception {
        logger.info("Adding new repetition to event with id {}", eventId);
        final String uri = BASE_URI + "/events/{eventId}/repetitions";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("eventId", eventId);

        RepetitionCreateDTO repetitionCreateDTO = repetitionDTOmapper.repetitionToRepetitionCreateDTO(repetition);

        Long repetitionId;
        try {
            HttpHeaders headers = utils.createAuthenticationHeader();
            HttpEntity entity = new HttpEntity(repetitionCreateDTO, headers);
            ResponseEntity response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class, params);

            String id = (String) response.getBody();
            repetitionId = objectMapper.readValue(id, Long.class);
            logger.info("Repetition successfully added to event with id {}", eventId);
        } catch (Exception ex) {
            logger.error("Error while adding repetition to event with id {}", eventId, ex);
            if (ex instanceof HttpStatusCodeException) {
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    throw new UnauthorizedException();
                }
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.FORBIDDEN) {
                    throw new AccessDeniedException();
                }
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.NOT_FOUND) {
                    throw new NotFoundException();
                }
            }

            throw ex;
        }

        return repetitionId;
    }

    public void updateRepetition(Repetition repetition, long repetitionId) throws Exception {
        logger.info("Updating repetition with id {}", repetitionId);
        final String uri = BASE_URI + "/repetitions/{repetitionId}";
        Map<String, Long> params = new HashMap<>();
        params.put("repetitionId", repetitionId);

        RepetitionCreateDTO repetitionCreateDTO = repetitionDTOmapper.repetitionToRepetitionCreateDTO(repetition);
        try {
            HttpHeaders headers = utils.createAuthenticationHeader();
            HttpEntity entity = new HttpEntity(repetitionCreateDTO, headers);
            restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class, params);

            logger.info("Repetition with id {} successfully updated", repetitionId);
        } catch (Exception ex) {
            logger.error("Error while updating repetition with id {}", repetitionId, ex);
            if (ex instanceof HttpStatusCodeException) {
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    throw new UnauthorizedException();
                }
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.FORBIDDEN) {
                    throw new AccessDeniedException();
                }
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.NOT_FOUND) {
                    throw new NotFoundException();
                }
            }

            throw ex;
        }
    }

    /**
     * Method used to mediate the deletion of given event.
     *
     * @param eventId ID of event that is going to be deleted.
     */
    public void deleteEvent(long eventId) throws Exception {
        logger.info("Deleting event with id {}", eventId);
        final String uri = BASE_URI + "/events/{eventId}";
        Map<String, Long> params = new HashMap<String, Long>();
        params.put("eventId", eventId);

        try {
            HttpHeaders headers = utils.createAuthenticationHeader();
            HttpEntity entity = new HttpEntity(headers);
            restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class, params);

            logger.info("Event with id {} successfully deleted", eventId);
        } catch (Exception ex) {
            logger.error("Error while deleting event with id {}", eventId, ex);
            if (ex instanceof HttpStatusCodeException) {
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    throw new UnauthorizedException();
                }
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.FORBIDDEN) {
                    throw new AccessDeniedException();
                }
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.NOT_FOUND) {
                    throw new NotFoundException();
                }
            }

            throw ex;
        }
    }

    public void deleteFromRepetition(long repetitionId, LocalDate date) throws Exception {
        logger.info("Deleting event at date {} from repetition with id {}", date, repetitionId);
        final String uri = BASE_URI + "/repetitions/{repetitionId}/events?date={date}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("repetitionId", repetitionId);
        params.put("date", date);

        try {
            HttpHeaders headers = utils.createAuthenticationHeader();
            HttpEntity entity = new HttpEntity(headers);
            restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class, params);

            logger.info("Event at date {} successfully deleted from repetition with id {}", date, repetitionId);
        } catch (ResourceAccessException | HttpStatusCodeException ex) {
            logger.error("Error while deleting event at date {} from repetition with id {}", date, repetitionId, ex);
            if (ex instanceof HttpStatusCodeException) {
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    throw new UnauthorizedException();
                }
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.FORBIDDEN) {
                    throw new AccessDeniedException();
                }
                if (((HttpStatusCodeException) ex).getStatusCode() == HttpStatus.NOT_FOUND) {
                    throw new NotFoundException();
                }
            }

            throw ex;
        }
    }
}