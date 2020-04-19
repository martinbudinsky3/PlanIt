package com.example.vavaplanit.Api;

import com.example.vavaplanit.Database.Service.EventService;
import com.example.vavaplanit.Model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    public ResponseEntity getAllByUserId(@PathVariable("userId") int userId){
        List<Event> eventList = eventService.getAllByUserId(userId);
        return new ResponseEntity<>(eventList, HttpStatus.OK);
    }

}
