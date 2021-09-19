package com.example.vavaplanit.api;

import com.example.vavaplanit.dto.event.EventCreateDTO;
import com.example.vavaplanit.dto.event.EventUpdateDTO;
import com.example.vavaplanit.dto.event.RepeatedEventUpdateDTO;
import com.example.vavaplanit.dto.mappers.EventDTOmapper;
import com.example.vavaplanit.dto.mappers.RepetitionDTOmapper;
import com.example.vavaplanit.dto.repetition.RepetitionCreateDTO;
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
    @Autowired
    private RepetitionDTOmapper repetitionDTOmapper;
    @Autowired
    private EventDTOmapper eventDTOmapper;


    @PutMapping("{repetitionId}/events")
    public ResponseEntity updateEventInRepetitionAtDate(Principal principal, @PathVariable("repetitionId") long repetitionId,
                                                        @RequestParam(value = "date") String date,
                                                        @RequestBody EventUpdateDTO eventUpdateDTO) {
        String username = principal.getName();
        long userId = userService.getUserByUsername(username).getId();

        Repetition repetition = repetitionService.getRepetitionById(repetitionId);
        if(repetition != null) {
            logger.info("Updating event at date {} in repetition with id {}", date, repetitionId);
            Event event = eventDTOmapper.eventUpdateDTOtoEvent(eventUpdateDTO);
            eventService.updateEventInRepetitionAtDate(userId, repetitionId, event, date);
            logger.info("Event at date {} successfully updated in repetition with id {}", date, repetitionId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            logger.error("Repetition with id {} does not exist", repetitionId);
            return ResponseEntity.status(HttpStatus.
                    NOT_FOUND).
                    body("Repetition with id: " + repetitionId + " does not exist");
        }
    }

    @PutMapping("{repetitionId}/events")
    public ResponseEntity updateAllEventsInRepetition(@PathVariable("repetitionId") long repetitionId,
                                                      @RequestBody RepeatedEventUpdateDTO repeatedEventUpdateDTO) {
        Repetition repetition = repetitionService.getRepetitionById(repetitionId);
        if(repetition != null) {
            logger.info("Updating all events of repetition with id {}", repetitionId);
            Event event = eventDTOmapper.repeatedEventUpdateDTOtoEvent(repeatedEventUpdateDTO);
            eventService.updateAllEventsInRepetition(repetitionId, event);
            logger.info("Events of repetition with id {} successfully updated", repetitionId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            logger.error("Repetition with id {} does not exist", repetitionId);
            return ResponseEntity.status(HttpStatus.
                    NOT_FOUND).
                    body("Repetition with id: " + repetitionId + " does not exist");
        }
    }

    @PutMapping("{repetitionId}")
    public ResponseEntity updateRepetition(@PathVariable("repetitionId") int repetitionId,
                                           @RequestBody RepetitionCreateDTO repetitionCreateDTO) {
        logger.info("Updating repetition with id {}", repetitionId);

        Repetition repetitionFromDb = repetitionService.getRepetitionById(repetitionId);
        if(repetitionFromDb != null) {
            Repetition repetition = repetitionDTOmapper.repetitionCreateDTOtoRepetition(repetitionCreateDTO);
            eventService.updateRepetition(repetitionId, repetition);
            logger.info("Repetition with id {} successfully updated", repetitionId);
            return ResponseEntity.ok().build();
        }

        logger.info("Repetition with id {} not found", repetitionId);
        return ResponseEntity.notFound().build();
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
