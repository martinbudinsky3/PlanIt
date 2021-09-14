package com.example.vavaplanit.api;

import com.example.vavaplanit.model.Event;
import com.example.vavaplanit.service.EventService;
import com.example.vavaplanit.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("repetitions")
public class RepetitionController {
    private Logger logger = LoggerFactory.getLogger(RepetitionController.class);
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;


    @PutMapping(value = "{repetitionId}/events", params = "date")
    // TODO authorization
    public ResponseEntity updateEventInRepetition(Principal principal, @PathVariable("repetitionId") int eventId,
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

    @PutMapping("{repetitionId}")
    // TODO authorization
    public ResponseEntity updateRepetition(@PathVariable("repetitionId") int repetitionId,
                                           @RequestBody Event event) {
        logger.info("Updating repetition with id " + repetitionId);
        Event eventFromDb = eventService.getEventWithRepetition(repetitionId);
        if(eventFromDb != null && eventFromDb.getRepetition() != null){
            eventService.updateRepetition(repetitionId, event, eventFromDb.getRepetition());
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

    @DeleteMapping("{repetitionId}/events")
    // TODO authorization
    public ResponseEntity deleteFromRepetition(@PathVariable("repetitionId") int eventId,
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
