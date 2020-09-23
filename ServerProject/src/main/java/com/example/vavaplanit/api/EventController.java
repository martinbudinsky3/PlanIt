package com.example.vavaplanit.api;

import com.example.vavaplanit.service.EventService;
import com.example.vavaplanit.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private EventService eventService;

    /**
     * Inserting new event
     * @param event new event
     * @return id of inserted event */
    @PostMapping
    public ResponseEntity addEvent(@RequestBody Event event) {
        logger.info("Inserting new Event. Title: " + event.getTitle());

        Integer id = eventService.add(event, event.getIdUser());
        if(id != null) {
            logger.info("Event successfully inserted. It's ID is: " + id);
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        }

        logger.error("Error inserting new event. HTTP Status: " + HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(value = "{idUser}")
    public ResponseEntity getEventsByDate(@PathVariable("idUser") int idUser, @RequestParam(value="date") String date) {
        logger.info("Getting events by user's id: " + idUser + ", date: " + date);
        List<Event> eventList = eventService.getEventsByDate(idUser, date);
        logger.info("Events from date " + date + " successfully found. Returning " + eventList.size() + " events.");
        return new ResponseEntity<>(eventList, HttpStatus.OK);
    }

    /**
     * @param idUser ID of user
     * @param month selected month
     * @param year selected year
     * @return list of all events that belong to user and starts dates of these events are in selected year and month. */
    @GetMapping(value = "{idUser}/{year}/{month}")
    public ResponseEntity getEventsByMonthAndUserId(@PathVariable("idUser") int idUser, @PathVariable("year") int year,
                                                    @PathVariable("month") int month) {
        logger.info("Getting events by user's id: " + idUser + ", year: " + year + " and month: " + month);
        List<Event> eventList = eventService.getEventsByMonthAndUserId(idUser, year, month);
        logger.info("Events from year " + year + " and month " + month + " successfully found. Returning " + eventList.size() + " events.");
        return new ResponseEntity<>(eventList, HttpStatus.OK);
    }

    /**
     * @param idUser ID of user
     * @param idEvent ID of event
     * @return event with entered ID */
    @GetMapping(value = "{idUser}/{idEvent}")
    public ResponseEntity getEvent(@PathVariable("idUser") int idUser, @PathVariable("idEvent") int idEvent,
                                   @RequestParam("date") String date) {
        logger.info("Getting user's [" + idUser + "] event [" + idEvent + "]");
        Event event = eventService.getEvent(idUser, idEvent, date);

        if (event != null){
            logger.info("Event successfully found. Returning event[" + event.getIdEvent() + "].");
            return new ResponseEntity<>(event, HttpStatus.OK);
        }

        logger.error("Error. Event not found. HTTP Status: " + HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * @param idUser ID of user
     * @return list of events with alert time in current minute. */
    @GetMapping(value="alert/{idUser}/{currentTime}")
    public ResponseEntity getEventsToAlert(@PathVariable("idUser") int idUser,
                                           @PathVariable("currentTime") String currentTime) {
        logger.info("Getting events to alert by user's id: " + idUser);
        List<Event> events = eventService.getEventsToAlert(idUser, currentTime);

        logger.info("Returning " + events.size() + " events to alert to user ["+ idUser +"].");

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    /**
     * Updating event
     * @param idEvent ID of event that is going to be updated
     * @param event Event with updated attributes */
    @PutMapping("{idUser}/{idEvent}")
    public ResponseEntity updateEvent(@PathVariable("idUser") int idUser, @PathVariable("idEvent") int idEvent,
                                      @RequestBody Event event) {
        logger.info("Updating event. Event's ID: " + idEvent);
        Event eventFromDb = eventService.getEvent(idUser, idEvent);
        if(eventFromDb != null){
            eventService.update(idEvent, event);
            logger.info("Event [" + idEvent + "] successfully updated.");
            return ResponseEntity.ok().build();
        } else {
            logger.error("Error. Event with id: " + idEvent + " does not exist.");
            return ResponseEntity.status(HttpStatus.
                    PRECONDITION_FAILED).
                    body("Event with id: " + idEvent + " does not exist");
        }
    }

    @PutMapping("{idUser}/{idEvent}/{date}")
    public ResponseEntity updateEventInRepetition(@PathVariable("idUser") int idUser, @PathVariable("idEvent") int idEvent,
                                      @PathVariable String date, @RequestBody Event event) {
        logger.info("Updating event in repetition. Event's ID: " + idEvent);
        Event eventFromDb = eventService.getEventWithRepetition(idUser, idEvent);
        if(eventFromDb != null && eventFromDb.getRepetition() != null){
            Integer id = eventService.updateEventInRepetition(idUser, event, date);
            logger.info("Event [" + idEvent + "] successfully updated in repetition.");
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        } else {
            logger.error("Error. Event or repetition with id: " + idEvent + " does not exist.");
            return ResponseEntity.status(HttpStatus.
                    PRECONDITION_FAILED).
                    body("Event or repetition with id: " + idEvent + " does not exist");
        }
    }

    @PutMapping("/repetition/{idUser}/{idEvent}")
    public ResponseEntity updateRepetition(@PathVariable("idUser") int idUser, @PathVariable("idEvent") int idEvent,
                                           @RequestBody Event event) {
        logger.info("Updating repetition. Event's ID: " + idEvent);
        Event eventFromDb = eventService.getEventWithRepetition(idUser, idEvent);
        if(eventFromDb != null && eventFromDb.getRepetition() != null){
            eventService.updateRepetition(idEvent, event);
            logger.info("Repetition [" + event.getRepetition().getEventId() + "] successfully updated.");
            return ResponseEntity.ok().build();
        } else if(eventFromDb != null && eventFromDb.getRepetition() == null) {
            eventService.updateEventAndAddRepetition(event);
            logger.info("Repetition [" + event.getRepetition().getEventId() + "] successfully added.");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            logger.error("Error. Event with id: " + event.getIdEvent() + " does not exist.");
            return ResponseEntity.status(HttpStatus.
                    PRECONDITION_FAILED).
                    body("Event with id: " + event.getIdEvent() + " does not exist");
        }
    }

    /**
     * Deleting event from calendar
     * @param idUser ID of user
     * @param idEvent ID of event
     * */
    @DeleteMapping("{idUser}/{idEvent}")
    public ResponseEntity delete(@PathVariable("idUser") int idUser, @PathVariable("idEvent") int idEvent) {
        logger.info("Deleting event. Event's ID: " + idEvent);
        Event event = eventService.getEventWithRepetition(idUser, idEvent);
        if(event != null){
            eventService.delete(event);
            logger.info("Event [" + idEvent + "] successfully deleted.");
            return ResponseEntity.ok().build();
        } else {
            logger.error("Error. Event with id: " + idEvent + " does not exist ");
            return ResponseEntity.status(HttpStatus.
                    PRECONDITION_FAILED).
                    body("Event with id: " + idEvent + " does not exist");
        }
    }

    @DeleteMapping("/repetition/{idUser}/{idEvent}/{date}")
    public ResponseEntity deleteFromRepetition(@PathVariable("idUser") int idUser, @PathVariable("idEvent") int idEvent,
                                               @PathVariable("date") String date) {
        logger.info("Deleting event from repetition. Event's ID: " + idEvent);
        Event eventFromDb = eventService.getEventWithRepetition(idUser, idEvent);
        if(eventFromDb != null && eventFromDb.getRepetition() != null){
            eventService.deleteFromRepetition(idEvent, date, eventFromDb.getExceptionId());
            logger.info("Event [" + idEvent + "] successfully deleted from repetition.");
            return ResponseEntity.ok().build();
        } else {
            logger.error("Error. Event with id: " + idEvent + " does not exist ");
            return ResponseEntity.status(HttpStatus.
                    PRECONDITION_FAILED).
                    body("Event with id: " + idEvent + " does not exist");
        }
    }
}
