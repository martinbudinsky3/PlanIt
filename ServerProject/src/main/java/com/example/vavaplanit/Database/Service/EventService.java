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

    public List<Event> getAllByUserId(int userId){
        return this.eventRepository.getAllByUserId(userId);
    }

    public List<Event> getEventsByMonthAndUserId(int userId, int year, int month){
        GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month - 1, 1);
        int daysInMonth = gregorianCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        LocalDate minDate = LocalDate.of(year, month, 1);
        LocalDate maxDate = LocalDate.of(year, month, daysInMonth);

        return this.eventRepository.getEventsByMonthAndUserId(userId, minDate, maxDate);
    }
}
