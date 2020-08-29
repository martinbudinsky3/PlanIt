package com.example.vavaplanit.service;

import com.example.vavaplanit.database.repository.EventRepository;
import com.example.vavaplanit.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    /**
     * Inserting new event
     * @param event Event object to be inserted
     * @param idUser ID of user
     * @return ID of inserted event*/
    public Integer add(Event event, int idUser) {
        Integer idEvent = eventRepository.add(event);
        if(idEvent != null) {
            idEvent = eventRepository.addEventUser(idUser, idEvent);
        }

        return idEvent;
    }

    public List<Event> getEventsByDate(int idUser, String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        return eventRepository.getEventsByDate(idUser, date);
    }

    /**
     * Getting all events that belong to user and starts dates of these events are in selected year and month.
     * It calculates boundaries in which would events starts date belong.
     * @param idUser ID of user
     * @param year selected year
     * @param month selected month
     * @return list of events
     */
    public List<Event> getEventsByMonthAndUserId(int idUser, int year, int month){
        LocalDate minDate = LocalDate.of(year, month, 1);
        LocalDate maxDate = minDate.plusMonths(1).minusDays(1);

        return this.eventRepository.getEventsByMonthAndUserId(idUser, minDate, maxDate);
    }

    /**
     * Getting event by it's ID
     * @param idEvent ID of the event*/
    public Event getEvent(int idEvent){
        return this.eventRepository.getEvent(idEvent);
    }

    /**
     * Getting event by it's ID and user's ID
     * @param idUser ID of the user
     * @param idEvent ID of the event*/
    public Event getUserEvent(int idUser, int idEvent){
        return this.eventRepository.getUserEvent(idUser, idEvent);
    }

    /**
     * Getting all events of user that have notifications set for current time and date
     * @param idUser ID of user*/
    public List<Event> getEventsToAlert(int idUser, String currentTimeString){
        LocalDateTime currentTime = LocalDateTime.parse(currentTimeString);
        DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String time = LocalTime.of(currentTime.getHour(), currentTime.getMinute()).format(dtfTime);
        String date = LocalDate.of(currentTime.getYear(), currentTime.getMonth(), currentTime.getDayOfMonth())
                .format(dtfDate);

        return this.eventRepository.getEventsToAlert(idUser, date, time);
    }

    /**
     * Update event
     * @param event event object which is going to be updated
     * @param id id of Event which is going to be updated*/
    public void update(int id, Event event){
        this.eventRepository.update(id, event);
    }

    /**
     * Delete event by user'd and event's id
     * @param idUser ID of user that wants to delete event
     * @param idEvent ID of Event which is going to be deleted*/
    public void delete(int idUser, int idEvent) {
        this.eventRepository.deleteFromUserEvent(idUser, idEvent);
        this.eventRepository.deleteFromEvent(idEvent);
    }


}
