package com.example.vavaplanit.Database.Service;

import com.example.vavaplanit.Database.Repository.EventRepository;
import com.example.vavaplanit.Model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public Integer add(Event event, int idUser) {
        Integer idEvent = eventRepository.add(event);
        if(idEvent != null) {
            idEvent = eventRepository.addEventUser(idUser, idEvent);
        }

        return idEvent;
    }

    public List<Event> getEventsByMonthAndUserId(int idUser, int year, int month){
        GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month - 1, 1);
        int daysInMonth = gregorianCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        LocalDate minDate = LocalDate.of(year, month, 1);
        LocalDate maxDate = LocalDate.of(year, month, daysInMonth);

        return this.eventRepository.getEventsByMonthAndUserId(idUser, minDate, maxDate);
    }

    public Event getEvent(int idEvent){
        return this.eventRepository.getEvent(idEvent);
    }

    public Event getUserEvent(int idUser, int idEvent){
        return this.eventRepository.getUserEvent(idUser, idEvent);
    }

    public List<Event> getEventsToAlert(int idUser){
        DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String time = LocalTime.now().withSecond(0).format(dtfTime);
        String date = LocalDate.now().format(dtfDate);

        return this.eventRepository.getEventsToAlert(idUser, date, time);
    }

    public void update(int id, Event event){
        this.eventRepository.update(id, event);
    }

    public void delete(int idUser, int idEvent) {
        this.eventRepository.deleteFromUserEvent(idUser, idEvent);
        this.eventRepository.deleteFromEvent(idEvent);
    }
}
