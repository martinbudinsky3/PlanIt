package com.example.vavaplanit.Database.Service;

import com.example.vavaplanit.Database.Repository.EventRepository;
import com.example.vavaplanit.Model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public List<Event> getAllByUserId(int idUser){
        return this.eventRepository.getAllByUserId(idUser);
    }

    public List<Event> getEventsByMonthAndUserId(int idUser, int year, int month){
        GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month - 1, 1);
        int daysInMonth = gregorianCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        LocalDate minDate = LocalDate.of(year, month, 1);
        LocalDate maxDate = LocalDate.of(year, month, daysInMonth);

        return this.eventRepository.getEventsByMonthAndUserId(idUser, minDate, maxDate);
    }

    public Event getEventByIdUserAndIdEvent(int idUser, int idEvent){
        return this.eventRepository.getEventByIdUserAndIdEvent(idUser,idEvent);
    }
}
