package com.example.vavaplanit.security.policies;

import com.example.vavaplanit.api.AuthController;
import com.example.vavaplanit.model.Event;
import com.example.vavaplanit.model.User;
import com.example.vavaplanit.service.EventService;
import com.example.vavaplanit.service.UserService;
import org.apache.tomcat.util.http.parser.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class RepetitionPolicy {
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;

    public boolean check(Authentication authentication, long repetitionId) {
        String username = authentication.getPrincipal().toString();
        User loggedInUser = userService.getUserByUsername(username);
        Event event = eventService.getEventByRepetitionId(repetitionId);

        return loggedInUser != null && event != null && loggedInUser.getId() == event.getAuthorId();
    }
}
