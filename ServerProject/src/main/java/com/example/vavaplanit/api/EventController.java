package com.example.vavaplanit.api;

import com.example.vavaplanit.service.EventService;
import com.example.vavaplanit.model.Event;
import com.example.vavaplanit.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("events")
public class EventController {

    private Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private EventService eventService;

    /**
     * Inserting new event
     * @param event new event
     * @return id of inserted event */
    @PostMapping
    public ResponseEntity addEvent(@RequestBody Event event) {
        logger.info("Inserting new event with title: " + event.getTitle());

        Long id = eventService.add(event, event.getAuthorId());
        if(id != null) {
            logger.info("Event successfully inserted with id " + id);
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        }

        logger.error("Error inserting new event. HTTP Status: " + HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * @param eventId ID of event
     * @return event with entered ID */
    // TODO find out when date param is used on frontend
    @GetMapping(value = "{eventId}")
    public ResponseEntity getEvent(@PathVariable("eventId") int eventId,
                                   @RequestParam(value = "date", required = false) String date) {
        logger.info("Getting event with id " + eventId);
        Event event = eventService.getEvent(eventId, date);

        if (event != null){
            logger.info("Event successfully found. Returning event with id " + event.getId());
            return new ResponseEntity<>(event, HttpStatus.OK);
        }

        logger.error("Error. Event with id" + eventId + " not found. HTTP Status: " + HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Updating event
     * @param eventId ID of event that is going to be updated
     * @param event Event with updated attributes */
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

    @DeleteMapping("{eventId}")
    public ResponseEntity delete(@PathVariable("eventId") int eventId) {
        logger.info("Deleting event with id " + eventId);
        Event event = eventService.getEvent(eventId);
        if(event != null){
            eventService.delete(eventId);
            logger.info("Event with id " + eventId + " successfully deleted.");
            return ResponseEntity.ok().build();
        } else {
            logger.error("Event with id " + eventId + " does not exist ");
            return ResponseEntity.status(HttpStatus.
                    PRECONDITION_FAILED).
                    body("Event with id " + eventId + " does not exist");
        }
    }
}
