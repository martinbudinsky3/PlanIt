package com.example.vavaplanit.api;

import com.example.vavaplanit.model.User;
import com.example.vavaplanit.service.EventService;
import com.example.vavaplanit.model.Event;
import com.example.vavaplanit.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("users/profile/events")
public class EventController {

    Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;

    /**
     * Inserting new event
     * @param event new event
     * @return id of inserted event */
    @PostMapping
    public ResponseEntity addEvent(@RequestBody Event event) {
        logger.info("Inserting new event with title: " + event.getTitle());

        Integer id = eventService.add(event, event.getIdUser());
        if(id != null) {
            logger.info("Event successfully inserted with id " + id);
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        }

        logger.error("Error inserting new event. HTTP Status: " + HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(params = "date")
    public ResponseEntity getEventsByDate(Principal principal, @RequestParam("date") String date) {
        String username = principal.getName();
        int userId = userService.getUserByUsername(username).getId();
        logger.info("Getting events of user: " + username + " at date: " + date);
        List<Event> eventList = eventService.getEventsByDate(userId, date);
        logger.info("Events from date " + date + " successfully found. Returning " + eventList.size() + " events.");
        return new ResponseEntity<>(eventList, HttpStatus.OK);
    }

    /**
     * @param month selected month
     * @param year selected year
     * @return list of all events that belong to user and starts dates of these events are in selected year and month. */
    @GetMapping(params = {"year", "month"})
    public ResponseEntity getEventsByMonthAndUserId(Principal principal, @RequestParam("year") int year,
                                                    @RequestParam("month") int month) {
        String username = principal.getName();
        int userId = userService.getUserByUsername(username).getId();
        logger.info("Getting events of user: " + username + "in year: " + year + " and month: " + month);
        List<Event> eventList = eventService.getEventsByMonthAndUserId(userId, year, month);
        logger.info("Events from year " + year + " and month " + month + " successfully found. Returning " + eventList.size() + " events.");
        return new ResponseEntity<>(eventList, HttpStatus.OK);
    }

    /**
     * @param eventId ID of event
     * @return event with entered ID */
    // TODO authorize
    @GetMapping(value = "{eventId}")
    public ResponseEntity getEvent(@PathVariable("eventId") int eventId,
                                   @RequestParam("date") String date) {
        logger.info("Getting event with id " + eventId);
        Event event = eventService.getEvent(eventId, date);

        if (event != null){
            logger.info("Event successfully found. Returning event with id " + event.getIdEvent());
            return new ResponseEntity<>(event, HttpStatus.OK);
        }

        logger.error("Error. Event with id" + eventId + " not found. HTTP Status: " + HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * @return list of events with alert time in current minute. */
    @GetMapping("alert")
    public ResponseEntity getEventsToAlert(Principal principal, @RequestParam("currentTime") String currentTime) {
        String username = principal.getName();
        int userId = userService.getUserByUsername(username).getId();
        logger.info("Getting events to alert of user " + username);
        List<Event> events = eventService.getEventsToAlert(userId, currentTime);

        logger.info("Returning " + events.size() + " events to alert of user " + username);

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    /**
     * Updating event
     * @param eventId ID of event that is going to be updated
     * @param event Event with updated attributes */
    // TODO authorize
    @PutMapping(value = "{eventId}")
    public ResponseEntity updateEvent(@PathVariable("eventId") int eventId,
                                      @RequestBody Event event) {
        logger.info("Updating event with id " + eventId);
        Event eventFromDb = eventService.getEvent(eventId);
        if(eventFromDb != null) {
            eventService.update(eventId, event);
            logger.info("Event with id " + eventId + " successfully updated.");
            return ResponseEntity.ok().build();
        } else {
            logger.error("Error. Event with id " + eventId + " does not exist.");
            return ResponseEntity.status(HttpStatus.
                    PRECONDITION_FAILED).
                    body("Event with id " + eventId + " does not exist");
        }
    }

    @PutMapping(value = "{eventId}", params = "date")
    // TODO authorization
    public ResponseEntity updateEventInRepetition(Principal principal, @PathVariable("eventId") int eventId,
                                      @RequestParam("date") String date, @RequestBody Event event) {
        String username = principal.getName();
        int userId = userService.getUserByUsername(username).getId();
        logger.info("Updating event with id " + eventId + "in repetition");
        Event eventFromDb = eventService.getEventWithRepetition(eventId);
        if(eventFromDb != null && eventFromDb.getRepetition() != null){
            event.setExceptionId(eventFromDb.getExceptionId());
            eventService.updateEventInRepetition(userId, eventId, event, date);
            logger.info("Event with id " + eventId + " successfully updated in repetition.");
            return new ResponseEntity<>(HttpStatus.OK); // TODO condition on response entity
        } else {
            logger.error("Error. Event or repetition with id: " + eventId + " does not exist.");
            return ResponseEntity.status(HttpStatus.
                    PRECONDITION_FAILED).
                    body("Event or repetition with id: " + eventId + " does not exist");
        }
    }

    @PutMapping("{eventId/repetition}")
    // TODO authorization
    public ResponseEntity updateRepetition(@PathVariable("eventId") int eventId,
                                           @RequestBody Event event) {
        logger.info("Updating repetition with id " + eventId);
        Event eventFromDb = eventService.getEventWithRepetition(eventId);
        if(eventFromDb != null && eventFromDb.getRepetition() != null){
            eventService.updateRepetition(eventId, event, eventFromDb.getRepetition());
            logger.info("Repetition [" + event.getRepetition().getEventId() + "] successfully updated.");
            return ResponseEntity.ok().build();
        } else if(eventFromDb != null && eventFromDb.getRepetition() == null) {
            eventService.updateEventAndAddRepetition(event);
            logger.info("Repetition [" + event.getRepetition().getEventId() + "] successfully added.");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            logger.error("Error. Event with id " + event.getIdEvent() + " does not exist.");
            return ResponseEntity.status(HttpStatus.
                    PRECONDITION_FAILED).
                    body("Event with id " + event.getIdEvent() + " does not exist");
        }
    }

    /**
     * Deleting event from calendar
     * @param eventId ID of event
     * */
    @DeleteMapping("{eventId}")
    // TODO authorization
    public ResponseEntity delete(@PathVariable("eventId") int eventId) {
        logger.info("Deleting event with id " + eventId);
        Event event = eventService.getEventWithRepetition(eventId);
        if(event != null){
            eventService.delete(event);
            logger.info("Event with id " + eventId + " successfully deleted.");
            return ResponseEntity.ok().build();
        } else {
            logger.error("Error. Event with id " + eventId + " does not exist ");
            return ResponseEntity.status(HttpStatus.
                    PRECONDITION_FAILED).
                    body("Event with id " + eventId + " does not exist");
        }
    }

    @DeleteMapping("{eventId}/repetition")
    // TODO authorization
    public ResponseEntity deleteFromRepetition(@PathVariable("eventId") int eventId,
                                               @RequestParam("date") String date) {
        logger.info("Deleting event with id " + eventId + " from repetition ");
        Event eventFromDb = eventService.getEventWithRepetition(eventId);
        if(eventFromDb != null && eventFromDb.getRepetition() != null){
            eventService.deleteFromRepetition(eventId, date, eventFromDb.getExceptionId());
            logger.info("Event with id " + eventId + " successfully deleted from repetition.");
            return ResponseEntity.ok().build();
        } else {
            logger.error("Error. Event with id " + eventId + " does not exist ");
            return ResponseEntity.status(HttpStatus.
                    PRECONDITION_FAILED).
                    body("Event with id " + eventId + " does not exist");
        }
    }
}
