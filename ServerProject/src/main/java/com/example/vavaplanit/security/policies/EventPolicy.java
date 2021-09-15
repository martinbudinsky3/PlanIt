package com.example.vavaplanit.security.policies;

import com.example.vavaplanit.model.Event;
import com.example.vavaplanit.model.User;
import com.example.vavaplanit.service.EventService;
import com.example.vavaplanit.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class EventPolicy {
    private Logger logger = LoggerFactory.getLogger(EventPolicy.class);

    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;

    public boolean check(Authentication authentication, int eventId) {
        String username = authentication.getPrincipal().toString();
        User loggedInUser = userService.getUserByUsername(username);
        Event event = eventService.getEvent(eventId);

        logger.debug("Logged in user id: {}",loggedInUser.getId());
        logger.debug("Event id: {}", event.getAuthorId());

        return loggedInUser.getId() == event.getAuthorId();
    }
}
