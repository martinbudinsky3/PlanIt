package com.example.vavaplanit.Api;

import com.example.vavaplanit.Database.Service.EventService;
import com.example.vavaplanit.Model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    Logger logger = LoggerFactory.getLogger(EventController.class);


    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity addEvent(@RequestBody Event event) {
        logger.info("Inserting new Event. Title: " + event.getTitle());

        Integer id = eventService.add(event, event.getIdUser());
        if(id != null) {
            logger.info("Event successfully inserted. It's ID is: " + id);
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        }

        logger.error("Error inserting new event, " + HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @RequestMapping(value = "{idUser}/{year}/{month}", method = RequestMethod.GET)
    public ResponseEntity getEventsByMonthAndUserId(@PathVariable("idUser") int idUser, @PathVariable("year") int year,
                                                    @PathVariable("month") int month)
    {
        logger.info("Getting events by user's id: " + idUser + ", year: " + year + " and month: " + month);
        List<Event> eventList = eventService.getEventsByMonthAndUserId(idUser, year, month);
        if (eventList.size() > 0){
            logger.info("Events successfully found. Returning events.");
            return new ResponseEntity<>(eventList, HttpStatus.OK);
        }

        logger.error("Error. Events not found, " + HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(eventList, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @RequestMapping(value = "{idUser}/{idEvent}", method = RequestMethod.GET)
    public ResponseEntity getEvent(@PathVariable("idEvent") int idEvent){
        logger.info("Getting event by user's: and event's id");
        Event event = eventService.getEvent(idEvent);

        if (event != null){
            logger.info("Event successfully found. Returning event.");
            return new ResponseEntity<>(event, HttpStatus.OK);
        }

        logger.error("Error. Event not found, " + HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(null, HttpStatus.OK);

    }

    @RequestMapping(value="alert/{idUser}", method = RequestMethod.GET)
    public ResponseEntity getEventsToAlert(@PathVariable("idUser") int idUser){
        logger.info("Getting events to alert by user's id: " + idUser);
        List<Event> events = eventService.getEventsToAlert(idUser);

        logger.info("Returning events to alert.");
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity updateEvent(@PathVariable("id") int id, @RequestBody Event event){
        logger.info("Updating event. Event's ID: " + id);
        if(eventService.getEvent(id) != null){
            logger.info("Event successfully updated.");
            eventService.update(id, event);
            return ResponseEntity.ok().build();
        } else {
            logger.error("Error. Event not found. ");
            return ResponseEntity.status(HttpStatus.
                    PRECONDITION_FAILED).
                    body("Event with id: " + id + " does not exist");
        }
    }

    @DeleteMapping("{idUser}/{idEvent}")
    public ResponseEntity delete(@PathVariable("idUser") int idUser, @PathVariable("idEvent") int idEvent){
        logger.info("Deleting event. Event's ID: " + idEvent);
        if(eventService.getUserEvent(idUser, idEvent) != null){
            logger.info("Event successfully deleted.");
            eventService.delete(idUser, idEvent);
            return ResponseEntity.ok().build();
        } else {
            logger.error("Error. Event not found. ");
            return ResponseEntity.status(HttpStatus.
                    PRECONDITION_FAILED).
                    body("Event with id: " + idEvent + " does not exist");
        }
    }
}
