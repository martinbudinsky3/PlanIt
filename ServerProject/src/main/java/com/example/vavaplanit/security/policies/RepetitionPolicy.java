package com.example.vavaplanit.security.policies;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.stereotype.Component;

@Component
public class RepetitionPolicy {

    public boolean check(Authorization authorization, int repetitionId) {
        return true;
    }
}
