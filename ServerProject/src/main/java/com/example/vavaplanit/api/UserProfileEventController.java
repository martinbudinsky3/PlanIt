package com.example.vavaplanit.api;


import com.example.vavaplanit.dto.event.EventItemDTO;
import com.example.vavaplanit.dto.mappers.EventDTOmapper;
import com.example.vavaplanit.model.Event;
import com.example.vavaplanit.service.EventService;
import com.example.vavaplanit.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("users/profile/events")
public class UserProfileEventController {
    private Logger logger = LoggerFactory.getLogger(UserProfileEventController.class);
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;
    @Autowired
    private EventDTOmapper eventDTOmapper;

    @GetMapping(params = "date")
    public ResponseEntity getEventsByDate(Principal principal, @RequestParam("date") String date) {
        String username = principal.getName();
        long userId = userService.getUserByUsername(username).getId();
        logger.info("Getting events of user {} at date {}", username, date);
        List<Event> events = eventService.getEventsByDate(userId, date);
        logger.info("Events from date {} successfully found. Returning {} events", date, events.size());
        List<EventItemDTO> eventItemDTOs = eventDTOmapper.eventsToEventItemDTOs(events);

        return new ResponseEntity<>(eventItemDTOs, HttpStatus.OK);
    }

    /**
     * @param month selected month
     * @param year selected year
     * @return list of all events that belong to user and starts dates of these events are in selected year and month. */
    @GetMapping(params = {"year", "month"})
    public ResponseEntity getEventsByMonthAndYear(Principal principal, @RequestParam("year") int year,
                                                  @RequestParam("month") int month) {
        String username = principal.getName();
        long userId = userService.getUserByUsername(username).getId();
        logger.info("Getting events of user {} from year {} and month {}", username, year, month);
        List<Event> events = eventService.getEventsByMonthAndUserId(userId, year, month);
        logger.info("Returning {} events from year {} and month {} successfully found", events.size(), year, month);
        List<EventItemDTO> eventItemDTOs = eventDTOmapper.eventsToEventItemDTOs(events);

        return new ResponseEntity<>(eventItemDTOs, HttpStatus.OK);
    }

    /**
     * @return list of events with alert time in current minute. */
    @GetMapping("alert")
    public ResponseEntity getEventsToAlert(Principal principal, @RequestParam("currentTime") String currentTime) {
        String username = principal.getName();
        long userId = userService.getUserByUsername(username).getId();
        logger.info("Getting events to alert of user {}", username);
        List<Event> events = eventService.getEventsToAlert(userId, currentTime);
        logger.info("Returning {} events to alert of user {}", events.size(), username);
        List<EventItemDTO> eventItemDTOs = eventDTOmapper.eventsToEventItemDTOs(events);

        return new ResponseEntity<>(eventItemDTOs, HttpStatus.OK);
    }
}
