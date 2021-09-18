package com.example.vavaplanit.api;

import com.example.vavaplanit.dto.event.EventCreateDTO;
import com.example.vavaplanit.dto.event.EventDetailDTO;
import com.example.vavaplanit.dto.event.EventUpdateDTO;
import com.example.vavaplanit.dto.mappers.EventDTOmapper;
import com.example.vavaplanit.service.EventService;
import com.example.vavaplanit.model.Event;
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
    private EventService eventService;
    @Autowired
    private UserService userService;
    @Autowired
    private EventDTOmapper eventDTOMapper;

    /**
     * Inserting new event
     * @param eventCreateDTO new event
     * @return id of inserted event */
    @PostMapping
    public ResponseEntity addEvent(Principal principal, @RequestBody EventCreateDTO eventCreateDTO) {
        String username = principal.getName();
        long userId = userService.getUserByUsername(username).getId();
        logger.info("Inserting new event {} of user with id {}", eventCreateDTO.toString(), userId);

        Event event = eventDTOMapper.eventCreateDTOtoEvent(eventCreateDTO);
        Long id = eventService.add(event, userId);
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
    @GetMapping(value = "{eventId}")
    public ResponseEntity getEvent(@PathVariable("eventId") int eventId,
                                   @RequestParam(value = "date", required = false) String date) {
        logger.info("Getting event with id " + eventId);
        Event event = eventService.getEvent(eventId, date);

        if (event != null){
            logger.info("Event successfully found. Returning event with id " + event.getId());
            EventDetailDTO eventDetailDTO = eventDTOMapper.eventToEventDetailDTO(event);
            return new ResponseEntity<>(eventDetailDTO, HttpStatus.OK);
        }

        logger.error("Error. Event with id" + eventId + " not found. HTTP Status: " + HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Updating event
     * @param eventId ID of event that is going to be updated
     * @param eventUpdateDTO Event with updated attributes */
    @PutMapping(value = "{eventId}")
    public ResponseEntity updateEvent(@PathVariable("eventId") int eventId,
                                      @RequestBody EventUpdateDTO eventUpdateDTO) {
        logger.info("Updating event with id " + eventId);
        Event event = eventDTOMapper.eventUpdateDTOtoEvent(eventUpdateDTO);
        Event eventFromDb = eventService.getEvent(eventId);
        if(eventFromDb != null) {
            eventService.update(eventId, event);
            logger.info("Event with id " + eventId + " successfully updated.");
            return ResponseEntity.noContent().build();
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
