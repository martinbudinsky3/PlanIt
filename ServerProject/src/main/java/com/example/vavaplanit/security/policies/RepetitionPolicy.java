package com.example.vavaplanit.security.policies;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.stereotype.Component;

@Component
public class RepetitionPolicy {

    public boolean check(Authorization authorization, long repetitionId) {
        // TODO get event by repetition id, check if author of repetition is current user
        return true;
    }
}
