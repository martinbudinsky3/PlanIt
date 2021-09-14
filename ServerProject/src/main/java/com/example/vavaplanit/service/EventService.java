package com.example.vavaplanit.service;

import com.example.vavaplanit.database.repository.EventRepository;
import com.example.vavaplanit.model.Event;
import com.example.vavaplanit.model.repetition.Repetition;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

// TODO more detailed error handling - with own exceptions thrown from service layer and caught in controllers

@Service
public class EventService {
    Logger logger = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private RepetitionService repetitionService;
    @Autowired
    private EventRepository eventRepository;

    /**
     * Inserting new event
     *
     * @param event  Event object to be inserted
     * @param idUser ID of user
     * @return ID of inserted event
     */
    @Transactional
    public Integer add(Event event, int idUser) {
        if (event.getRepetition() != null) {
            LocalDate newStartDate = this.repetitionService.validateStart(event.getRepetition());
            setEventsDates(event, newStartDate);

            logger.debug("Adding event: { date: " + event.getDate() + ", endDate:" + event.getEndsDate() + ", alertDate: "
                    + event.getAlertDate() + "}");
        }

        Integer idEvent = eventRepository.add(event);
        this.eventRepository.addEventUser(idUser, idEvent);

        if (event.getRepetition() != null) {
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
     *
     * @param idUser ID of user
     * @param year   selected year
     * @param month  selected month
     * @return list of events
     */
    public List<Event> getEventsByMonthAndUserId(int idUser, int year, int month) {
        LocalDate minDate = LocalDate.of(year, month, 1);
        LocalDate maxDate = minDate.plusMonths(1);

        List<Event> events = this.eventRepository.getEventsByMonthAndUserId(idUser, minDate, maxDate);
        List<Event> eventsInMonth = new ArrayList<>();

        for (Event event : events) {
            List<LocalDate> dates = this.repetitionService.getEventDates(event.getIdEvent(), month, year);
            if (!dates.isEmpty()) {
                event.setDates(dates);
                eventsInMonth.add(event);
            } else if (!event.getDate().isBefore(minDate)) {
                eventsInMonth.add(event);
            }
        }

        return eventsInMonth;
    }

    /**
     * Getting event by it's ID
     *
     * @param idEvent ID of the event
     */
    public Event getEvent(int idEvent) {
        return this.eventRepository.getEvent(idEvent);
    }

    /**
     * Getting event by it's ID
     *
     * @param idEvent ID of the event
     */
    public Event getEventWithRepetition(int idEvent) {
        Event event = this.eventRepository.getEvent(idEvent);
        event.setRepetition(this.repetitionService.getRepetitionByEventIdOrExceptionId(idEvent, event.getExceptionId()));

        return event;
    }

    public Event getEvent(int eventId, String dateString) {
        Event event = this.eventRepository.getEvent(eventId);

        event.setRepetition(this.repetitionService.getRepetitionByEventIdOrExceptionId(eventId, event.getExceptionId()));

        if(dateString != null) {
            LocalDate date = LocalDate.parse(dateString);
            // if event is repeated and if event isn't exception in repetition count new event's dates
            if (event.getRepetition() != null && event.getIdEvent() == event.getRepetition().getEventId()
                    && this.repetitionService.checkDate(eventId, date)) {
                event = setEventsDates(event, date);
            }
        }

        return event;
    }

    /**
     * Getting all events of user that have notifications set for current time and date
     *
     * @param idUser ID of user
     */
    public List<Event> getEventsToAlert(int idUser, String currentTimeString) {
        LocalDateTime currentTime = LocalDateTime.parse(currentTimeString);
        DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String time = LocalTime.of(currentTime.getHour(), currentTime.getMinute()).format(dtfTime);
        String date = LocalDate.of(currentTime.getYear(), currentTime.getMonth(), currentTime.getDayOfMonth())
                .format(dtfDate);

        List<Event> eventsToAlert = this.eventRepository.getEventsToAlert(idUser, date, time);
        eventsToAlert.forEach(event -> {
            Repetition repetition = this.repetitionService.getRepetitionByEventIdOrExceptionId(event.getIdEvent(), event.getExceptionId());
            event.setRepetition(repetition);
        });

        return eventsToAlert;
    }

    /**
     * Update event
     *
     * @param event   event object which is going to be updated
     * @param eventId id of Event which is going to be updated
     */
    public void update(int eventId, Event event) {
        this.eventRepository.update(eventId, event);
    }

    @Transactional
    public void updateEventInRepetition(int userId, int eventId, Event event, String dateString) {
        LocalDate date = LocalDate.parse(dateString);

        if(event.getExceptionId() == 0) {
            Integer newExceptionId = repetitionService.addException(event.getRepetition().getEventId(), date);

            if (newExceptionId != 0) {
                event.setExceptionId(newExceptionId);
                Integer updatedEventId = this.eventRepository.add(event);

                if (updatedEventId != null) {
                    this.eventRepository.addEventUser(userId, updatedEventId);
                }
            }
        } else {
            update(eventId, event);
        }
    }

    @Transactional
    public void updateRepetition(int id, Event event, Repetition repetition) {
        // if repetition object is updated, delete exceptions from old repetition
        if(!event.getRepetition().equals(repetition)) {
            this.repetitionService.deleteExceptionsByRepetitionId(repetition.getEventId());
            this.repetitionService.update(event.getRepetition());

            logger.debug("Old repetition" + repetition.toString());
            logger.debug("Updated repetition" + event.getRepetition().toString());
        }

        // set new valid dates to event
        LocalDate newStartDate = this.repetitionService.validateStart(event.getRepetition());
        setEventsDates(event, newStartDate);

        this.eventRepository.update(event.getRepetition().getEventId(), event);
    }

    @Transactional
    public Integer updateEventAndAddRepetition(Event event) {
        LocalDate newStartDate = this.repetitionService.validateStart(event.getRepetition());
        setEventsDates(event, newStartDate);

        this.eventRepository.update(event.getIdEvent(), event);

        return this.repetitionService.addRepetition(event.getRepetition());
    }

    /**
     * Delete event by event's id
     *
     * @param idEvent ID of Event which is going to be deleted
     */
    public void delete(int idEvent) {
        this.eventRepository.delete(idEvent);
    }

    public void delete(Event event) {
        delete(event.getIdEvent());

        if(event.getRepetition() != null) {
            delete(event.getRepetition().getEventId());
        }
    }

    public void deleteFromRepetition(int idEvent, String dateString, Integer exceptionId) {
        LocalDate date = LocalDate.parse(dateString);

        if (exceptionId == null || exceptionId == 0) { // if event isn't exception in repetition add exception
            this.repetitionService.addException(idEvent, date);
        } else {  // if event already is exception, just delete it
            delete(idEvent);
        }
    }

    private Event setEventsDates(Event event, LocalDate newStart) {
        LocalDate start = event.getDate();
        LocalDate end = event.getEndsDate();
        LocalDate alert = event.getAlertDate();

        event.setDate(newStart);
        event.setEndsDate(countEndDate(start, end, newStart));
        event.setAlertDate(countAlertDate(start, alert, newStart));

        return event;
    }

    private LocalDate countEndDate(LocalDate defaultStart, LocalDate defaultEnd, LocalDate date) {
        int dayDiff = getDayDiff(defaultStart, defaultEnd);
        logger.debug("Counting end date - day diff = " + dayDiff);
        return date.plusDays(dayDiff);
    }

    private LocalDate countAlertDate(LocalDate defaultStart, LocalDate defaultAlert, LocalDate date) {
        int dayDiff = getDayDiff(defaultAlert, defaultStart);
        logger.debug("Counting alert date - day diff = " + dayDiff);
        return date.minusDays(dayDiff);
    }

    private int getDayDiff(LocalDate date1, LocalDate date2) {
        DateTime dateTime1 = new DateTime().withDate(date1.getYear(), date1.getMonthValue(), date1.getDayOfMonth());
        DateTime dateTime2 = new DateTime().withDate(date2.getYear(), date2.getMonthValue(), date2.getDayOfMonth());

        logger.debug("Date 1 in getDayDiff = " + date1);
        logger.debug("Date 2 in getDayDiff = " + date2);

        return Days.daysBetween(dateTime1, dateTime2).getDays();
    }
}
