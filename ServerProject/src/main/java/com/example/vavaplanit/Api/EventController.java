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

    @PostMapping
    public ResponseEntity addEvent(@RequestBody Event event) {
        Integer id = eventService.add(event, event.getIdUser());
        if(id != null) {
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = "{idUser}", method = RequestMethod.GET)
    public ResponseEntity getAllByUserId(@PathVariable("idUser") int idUser){
        List<Event> eventList = eventService.getAllByUserId(idUser);
        return new ResponseEntity<>(eventList, HttpStatus.OK);
    }

    @RequestMapping(value = "{idUser}/{year}/{month}", method = RequestMethod.GET)
    public ResponseEntity getEventsByMonthAndUserId(@PathVariable("idUser") int idUser, @PathVariable("year") int year,
                                                    @PathVariable("month") int month) {
        List<Event> eventList = eventService.getEventsByMonthAndUserId(idUser, year, month);
        return new ResponseEntity<>(eventList, HttpStatus.OK);
    }

    @RequestMapping(value = "{idUser}/{idEvent}", method = RequestMethod.GET)
    public ResponseEntity getEventByIdUserAndIdEvent(@PathVariable("idUser") int idUser,
                                                     @PathVariable("idEvent") int idEvent){
        Event event = eventService.getEventByIdUserAndIdEvent(idUser,idEvent);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

}
