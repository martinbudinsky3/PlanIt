package com.example.vavaplanit.api;

import com.example.vavaplanit.dto.event.EventCreateDTO;
import com.example.vavaplanit.dto.event.EventDetailDTO;
import com.example.vavaplanit.dto.event.EventPostponeDTO;
import com.example.vavaplanit.dto.event.EventUpdateDTO;
import com.example.vavaplanit.dto.mappers.EventDTOmapper;
import com.example.vavaplanit.dto.mappers.RepetitionDTOmapper;
import com.example.vavaplanit.dto.repetition.RepetitionCreateDTO;
import com.example.vavaplanit.model.repetition.Repetition;
import com.example.vavaplanit.service.EventService;
import com.example.vavaplanit.model.Event;
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
@RequestMapping("events")
public class EventController {

    private Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private RepetitionService repetitionService;
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;
    @Autowired
    private EventDTOmapper eventDTOMapper;
    @Autowired
    private RepetitionDTOmapper repetitionDTOmapper;

    /**
     * Inserting new event
     * @param eventCreateDTO new event
     * @return id of inserted event */
    @PostMapping
    public ResponseEntity addEvent(Principal principal, @RequestBody EventCreateDTO eventCreateDTO) {
        String username = principal.getName();
        long userId = userService.getUserByUsername(username).getId();
        logger.info("Inserting new event {} of user with id {}", eventCreateDTO.toString(), userId);

        logger.debug("eventCreateDTO: '{}'", eventCreateDTO);
        Event event = eventDTOMapper.eventCreateDTOtoEvent(eventCreateDTO);

        Long id = eventService.add(event, userId);
        if(id != null) {
            logger.info("Event successfully inserted with id {}", id);
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        }

        logger.error("Error inserting new event {}", event);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("{eventId}/repetitions")
    public ResponseEntity addEventRepetition(@PathVariable("eventId") long eventId,
                                             @RequestBody RepetitionCreateDTO repetitionCreateDTO) {
        logger.info("Inserting new repetition of event with id {}", eventId);

        Event event = eventService.getEvent(eventId);
        if(event != null) {
            Repetition repetition = repetitionDTOmapper.repetitionCreateDTOtoRepetition(repetitionCreateDTO);
            Long id = repetitionService.addRepetition(repetition, eventId);
            if(id != null) {
                logger.info("Event successfully inserted with id {}", id);
                return new ResponseEntity<>(id, HttpStatus.CREATED);
            }

            logger.error("Error while inserting new repetition of event with id {}", eventId);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.error("Event with id {} not found", eventId);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * @param eventId ID of event
     * @return event with entered ID */
    @GetMapping(value = "{eventId}")
    public ResponseEntity getEvent(@PathVariable("eventId") int eventId,
                                   @RequestParam(value = "date", required = false) String date) {
        logger.info("Getting event with id {}", eventId);
        Event event = eventService.getEvent(eventId, date);

        if (event != null){
            logger.info("Event successfully found. Returning event with id {}", event.getId());
            EventDetailDTO eventDetailDTO = eventDTOMapper.eventToEventDetailDTO(event);
            return new ResponseEntity<>(eventDetailDTO, HttpStatus.OK);
        }

        logger.error("Error. Event with id {} not found", eventId);
        return new ResponseEntity<>("Event with id " + eventId + " does not exist",
                HttpStatus.NOT_FOUND);
    }

    /**
     * Updating event
     * @param eventId ID of event that is going to be updated
     * @param eventUpdateDTO Event with updated attributes */
    @PutMapping(value = "{eventId}")
    public ResponseEntity updateEvent(@PathVariable("eventId") int eventId,
                                      @RequestBody EventUpdateDTO eventUpdateDTO) {
        logger.info("Updating event with id {}", eventId);
        Event event = eventDTOMapper.eventUpdateDTOtoEvent(eventUpdateDTO);
        Event eventFromDb = eventService.getEvent(eventId);
        if(eventFromDb != null) {
            eventService.update(eventId, event);
            logger.info("Event with id {} successfully updated", eventId);
            return ResponseEntity.noContent().build();
        } else {
            logger.error("Error. Event with id {} does not exist", eventId);
            return new ResponseEntity("Event with id " + eventId + " does not exist",
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "{eventId}/alert")
    public ResponseEntity postponeEvent(Principal principal, @PathVariable("eventId") int eventId,
                                      @RequestBody EventPostponeDTO eventPostponeDTO) {
        logger.info("Postponing event with id {}", eventId);
        String username = principal.getName();
        long userId = userService.getUserByUsername(username).getId();
        Event event = eventDTOMapper.eventPostponeDTOtoEvent(eventPostponeDTO);
        Event eventFromDb = eventService.getEvent(eventId);
        if(eventFromDb != null) {
            eventService.postponeEvent(userId, eventId, event, eventFromDb);
            logger.info("Event with id {} successfully postponed", eventId);
            return ResponseEntity.noContent().build();
        } else {
            logger.error("Error. Event with id {} does not exist", eventId);
            return new ResponseEntity("Event with id " + eventId + " does not exist",
                    HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{eventId}")
    public ResponseEntity delete(@PathVariable("eventId") int eventId) {
        logger.info("Deleting event with id {}", eventId);
        Event event = eventService.getEvent(eventId);
        if(event != null){
            eventService.delete(eventId);
            logger.info("Event with id {} successfully deleted", eventId);
            return ResponseEntity.ok().build();
        } else {
            logger.error("Event with id {} does not exist", eventId);
            return new ResponseEntity("Event with id " + eventId + " does not exist",
                    HttpStatus.NOT_FOUND);
        }
    }
}
