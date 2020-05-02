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
    public ResponseEntity getEvent(@PathVariable("idEvent") int idEvent){
        Event event = eventService.getEvent(idEvent);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @RequestMapping(value="alert/{idUser}", method = RequestMethod.GET)
    public ResponseEntity getEventToAlert(@PathVariable("idUser") int idUser){
        Event event = eventService.getEventToAlert(idUser);
        if(event != null) {
            return new ResponseEntity<>(event, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("{id}")
    public ResponseEntity updateEvent(@PathVariable("id") int id, @RequestBody Event event){
        if(eventService.getEvent(id) != null){
            eventService.update(id, event);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.
                    PRECONDITION_FAILED).
                    body("Event with id: " + id + " does not exist");
        }
    }

    @DeleteMapping("{idUser}/{idEvent}")
    public ResponseEntity delete(@PathVariable("idUser") int idUser, @PathVariable("idEvent") int idEvent){
        if(eventService.getUserEvent(idUser, idEvent) != null){
            eventService.delete(idUser, idEvent);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.
                    PRECONDITION_FAILED).
                    body("Event with id: " + idEvent + " does not exist");
        }
    }
}
