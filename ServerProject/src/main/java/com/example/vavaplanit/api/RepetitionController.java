package com.example.vavaplanit.api;

import com.example.vavaplanit.model.Event;
import com.example.vavaplanit.model.repetition.Repetition;
import com.example.vavaplanit.service.EventService;
import com.example.vavaplanit.service.RepetitionService;
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
    private RepetitionService repetitionService;
    @Autowired
    private UserService userService;


    @PutMapping("{repetitionId}/events")
    public ResponseEntity updateEventInRepetition(Principal principal, @PathVariable("repetitionId") long repetitionId,
                                                  @RequestParam("date") String date, @RequestBody Event event) {
        String username = principal.getName();
        long userId = userService.getUserByUsername(username).getId();
        logger.info("Updating event with id " + repetitionId + "in repetition");

        Repetition repetition = repetitionService.getRepetitionById(repetitionId);
        if(repetition != null) {
            eventService.updateEventInRepetition(userId, repetitionId, event, date);
            logger.info("Event at date " + date + " successfully updated in repetition with id " + repetitionId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            logger.error("Repetition with id " + repetitionId + " does not exist.");
            return ResponseEntity.status(HttpStatus.
                    PRECONDITION_FAILED).
                    body("Repetition with id: " + repetitionId + " does not exist");
        }
    }

    @PutMapping("{repetitionId}")
    public ResponseEntity updateRepetition(@PathVariable("repetitionId") int repetitionId,
                                           @RequestBody Event event) {
        logger.info("Updating repetition with id " + repetitionId);

        Repetition repetition = repetitionService.getRepetitionById(repetitionId);
        if(repetition != null && repetition.getEventId() == event.getId()) {
            eventService.updateRepetition(repetitionId, event, repetition);
            logger.info("Repetition with id " + repetition.getId() + " successfully updated.");
            return ResponseEntity.ok().build();
        } else {
            eventService.updateEventAndAddRepetition(event);
            logger.info("Event with id " + event.getId() + " successfully updated.");
            logger.info("Repetition with id " + repetition.getId() + " successfully added.");
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @DeleteMapping("{repetitionId}/events")
    public ResponseEntity deleteFromRepetition(@PathVariable("repetitionId") int repetitionId,
                                               @RequestParam("date") String date) {
        logger.info("Deleting event at date " + date + " from repetition with id " + repetitionId);

        Repetition repetition = repetitionService.getRepetitionById(repetitionId);
        if(repetition != null) {
            eventService.deleteFromRepetition(repetitionId, date);
            logger.info("Event with id " + repetitionId + " successfully deleted from repetition.");
            return ResponseEntity.ok().build();
        } else {
            logger.error("Repetition with id " + repetitionId + " does not exist ");
            return ResponseEntity.status(HttpStatus.
                    PRECONDITION_FAILED).
                    body("Repetition with id " + repetitionId + " does not exist");
        }
    }
}
