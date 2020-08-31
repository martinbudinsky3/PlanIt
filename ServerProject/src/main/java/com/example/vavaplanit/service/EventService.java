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
import java.util.Calendar;
import java.util.GregorianCalendar;
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
    public Integer add(Event event, int idUser) {
        Integer idEvent = eventRepository.add(event);
//        if(idEvent != null) {
        idEvent = this.eventRepository.addEventUser(idUser, idEvent);
//        }
        if(event.getRepetition() != null) {
            this.repetitionService.add(event.getRepetition());
        }

        return idEvent;
    }

    public List<Event> getEventsByDate(int idUser, String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        return this.eventRepository.getEventsByDate(idUser, date);
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
        List<Event> filteredOutEvents = new ArrayList<>();

        for(Event event : events) {
            List<LocalDate> dates = this.repetitionService.getEventDates(event.getIdEvent(), month, year);
            if(dates.isEmpty() && event.getDate().isBefore(minDate)) {
                filteredOutEvents.add(event);
                continue;
            }

            event.setDates(dates);
            event.setEndsDates(countEndDates(event.getDate(), event.getEndsDate(), dates));
            event.setAlertDates(countAlertDates(event.getDate(), event.getAlertDate(), dates));
        }

        events.removeAll(filteredOutEvents);

        return events;
    }

    /**
     * Getting event by it's ID
     * @param idEvent ID of the event*/
    @Transactional
    public Event getEvent(int idEvent){
        Event event = this.eventRepository.getEvent(idEvent);
        event.setRepetition(this.repetitionService.getRepetitionByEventId(idEvent));
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

    public void updateRepetition(int id, Event event){
//      int repetitionEventId = this.repetitionService.update(id);
//        if(repetitionEventId != id) {
//            delete(id);
//        }
        update(id, event);
    }

    /**
     * Delete event by user'd and event's id
     * @param idUser ID of user that wants to delete event
     * @param idEvent ID of Event which is going to be deleted*/
    // TODO in DB make FK on delete cascade instead
    @Transactional
    public void delete(int idUser, int idEvent) {
        this.eventRepository.deleteFromUserEvent(idUser, idEvent);
        this.eventRepository.deleteFromEvent(idEvent);
    }

    private List<LocalDate> countEndDates(LocalDate date1, LocalDate date2, List<LocalDate> dates) {
        int dayDiff = getDayDiff(date1, date2);

        return dates.stream().map(date -> date.plusDays(dayDiff)).collect(Collectors.toList());
    }

    private List<LocalDate> countAlertDates(LocalDate date1, LocalDate date2, List<LocalDate> dates) {
        int dayDiff = getDayDiff(date1, date2);

        return dates.stream().map(date -> date.minusDays(dayDiff)).collect(Collectors.toList());
    }

    private int getDayDiff(LocalDate date1, LocalDate date2) {
        DateTime dateTime1 = new DateTime().withDate(date1.getYear(), date1.getMonthValue(), date1.getDayOfMonth());
        DateTime dateTime2 = new DateTime().withDate(date2.getYear(), date2.getMonthValue(), date2.getDayOfMonth());

        return Days.daysBetween(dateTime1, dateTime2).getDays();
    }
}
