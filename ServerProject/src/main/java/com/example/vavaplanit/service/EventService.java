package com.example.vavaplanit.service;

import com.example.vavaplanit.database.repository.EventRepository;
import com.example.vavaplanit.model.Event;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    @Autowired
    private RepetitionService repetitionService;
    @Autowired
    private EventRepository eventRepository;

    /**
     * Inserting new event
     * @param event Event object to be inserted
     * @param idUser ID of user
     * @return ID of inserted event*/
    @Transactional
    public Long add(Event event, long idUser) {
        Long idEvent = eventRepository.add(event);
        this.eventRepository.addEventUser(idUser, idEvent);

        if(event.getRepetition() != null) {
            event.getRepetition().setEventId(idEvent);
            this.repetitionService.addRepetition(event.getRepetition());
        }

        return idEvent;
    }

    public List<Event> getEventsByDate(int idUser, String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        List<Event> events = this.eventRepository.getEventsByDate(idUser, date);

        return events.stream().filter(event -> this.repetitionService.checkDate(event.getIdEvent(), date) ||
                event.getDate().equals(date)).collect(Collectors.toList());
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
        LocalDate maxDate = minDate.plusMonths(1);

        List<Event> events = this.eventRepository.getEventsByMonthAndUserId(idUser, minDate, maxDate);
        List<Event> eventsInMonth = new ArrayList<>();

        for(Event event : events) {
            List<LocalDate> dates = this.repetitionService.getEventDates(event.getIdEvent(), month, year);
            if(!dates.isEmpty() || !event.getDate().isBefore(minDate)) {
                event.setDates(dates);
                eventsInMonth.add(event);
            }
        }

        return eventsInMonth;
    }

    /**
     * Getting event by it's ID
     * @param idEvent ID of the event*/
    public Event getEvent(long idEvent){
        Event event = this.eventRepository.getEvent(idEvent);
        event.setRepetition(this.repetitionService.getRepetitionByEventIdOrExceptionId(idEvent, event.getExceptionId()));

        return event;
    }

    /**
     * Getting event by it's ID
     * @param idEvent ID of the event*/
    // TODO add date to parameter, check date in Repetition, set date to event and count end and alert date of event
    public Event getEvent(long idEvent, String dateString){
        LocalDate date = LocalDate.parse(dateString);
        Event event = this.eventRepository.getEvent(idEvent);

        if(this.repetitionService.checkDate(idEvent, date)) {
            event.setRepetition(this.repetitionService.getRepetitionByEventIdOrExceptionId(idEvent, event.getExceptionId()));
            event.setDate(date);
            event.setEndsDate(countEndDate(event.getDate(), event.getEndsDate(), date));
            event.setAlertDate(countAlertDate(event.getDate(), event.getAlertDate(), date));
        }

        return event;
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
    // TODO new alert logic
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
    @Transactional
    public void update(long id, Event event, Long exceptionId){
        if(event.getRepetition() == null || exceptionId != null) {
            this.eventRepository.update(id, event);
        } else {
            Long newExceptionId = repetitionService.addException(id, event.getDate());
            if(newExceptionId != null) {
                event.setExceptionId(newExceptionId);
                this.eventRepository.add(event);
            }
        }
    }

    @Transactional
    public void updateRepetition(int id, Event event){
        this.repetitionService.update(event.getRepetition());
        if(event.getRepetition().getEventId() != id) {
            delete(id);
        }

        this.eventRepository.update(event.getRepetition().getEventId(), event);
    }

    /**
     * Delete event by event's id
     * @param idEvent ID of Event which is going to be deleted */
    public void delete(int idEvent) {
        this.eventRepository.delete(idEvent);
    }

    public void deleteFromRepetition(long idEvent, String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        this.repetitionService.addException(idEvent, date);
    }

    private LocalDate countEndDate(LocalDate defaultStart, LocalDate defaultEnd, LocalDate date) {
        int dayDiff = getDayDiff(defaultStart, defaultEnd);

        return date.plusDays(dayDiff);
    }

    private LocalDate countAlertDate(LocalDate defaultStart, LocalDate defaultAlert, LocalDate date) {
        int dayDiff = getDayDiff(defaultStart, defaultAlert);

        return date.minusDays(dayDiff);
    }

    private int getDayDiff(LocalDate date1, LocalDate date2) {
        DateTime dateTime1 = new DateTime().withDate(date1.getYear(), date1.getMonthValue(), date1.getDayOfMonth());
        DateTime dateTime2 = new DateTime().withDate(date2.getYear(), date2.getMonthValue(), date2.getDayOfMonth());

        return Days.daysBetween(dateTime1, dateTime2).getDays();
    }
}
