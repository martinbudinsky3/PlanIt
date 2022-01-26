package com.example.vavaplanit.service;

import com.example.vavaplanit.database.repository.EventRepository;
import com.example.vavaplanit.model.Event;
import com.example.vavaplanit.model.Exception;
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
     * @param userId ID of user
     * @return ID of inserted event
     */
    @Transactional
    public Long add(Event event, long userId) {
        if (event.getRepetition() != null) {
            LocalDate newStartDate = this.repetitionService.validateStart(event.getRepetition());
            event = setEventsDates(event, newStartDate);

            logger.debug("Adding event: { date: " + event.getStartDate() + ", endDate:" + event.getEndDate() + ", alertDate: "
                    + event.getAlertDate() + "}");
        }

        Long eventId = eventRepository.add(event, userId);

        if (event.getRepetition() != null) {
            this.repetitionService.addRepetition(event.getRepetition(), eventId);
        }

        return eventId;
    }

    public List<Event> getEventsByDate(long userId, String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        List<Event> events = this.eventRepository.getEventsByDate(userId, date);

        return events.stream().filter(event -> this.repetitionService.checkDate(event.getId(), date) ||
                event.getStartDate().equals(date)).collect(Collectors.toList());
    }

    /**
     * Getting all events that belong to user and starts dates of these events are in selected year and month.
     * It calculates boundaries in which would events starts date belong.
     *
     * @param userId ID of user
     * @param year   selected year
     * @param month  selected month
     * @return list of events
     */
    public List<Event> getEventsByMonthAndUserId(long userId, int year, int month) {
        LocalDate minDate = LocalDate.of(year, month, 1);
        LocalDate maxDate = minDate.plusMonths(1).minusDays(1);

        List<Event> events = this.eventRepository.getEventsByMonthAndUserId(userId, minDate, maxDate);
        List<Event> eventsInMonth = new ArrayList<>();

        for (Event event : events) {
            List<LocalDate> dates = this.repetitionService.getEventDates(event.getId(), month, year);
            if(dates == null) { // event has no repetition
                eventsInMonth.add(event);
            } else if (!dates.isEmpty()) { // event is repeated and has occurrence in requested month
                event.setDates(dates);
                eventsInMonth.add(event);
            }
        }

        return eventsInMonth;
    }
    
    public Event getEvent(long eventId) {
        return this.eventRepository.getEvent(eventId);
    }

    public Event getEvent(int eventId, String dateString) {
        Event event = this.getEvent(eventId);
        if(event == null) {
            return null;
        }

        Repetition repetition = this.repetitionService.getRepetitionByEventId(eventId);
        if(repetition == null) {
            repetition = this.repetitionService.getRepetitionByEventIdViaException(eventId);
            if(repetition != null) {
                event.setExceptionInRepetition(true);
            }
        }
        event.setRepetition(repetition);

        if(dateString != null && event.getRepetition() != null) {
            LocalDate date = LocalDate.parse(dateString);
            if (this.repetitionService.checkDate(eventId, date)) {
                event = setEventsDates(event, date);
            }
        }

        return event;
    }

    /**
     * Getting all events of user that have notifications set for current time and date
     *
     * @param userId ID of user
     */
    public List<Event> getEventsToAlert(long userId, String currentDateTimeString) {
        LocalDateTime currentDateTime = LocalDateTime.parse(currentDateTimeString);
        LocalDate currentDate = currentDateTime.toLocalDate();
        LocalTime currentTime = currentDateTime.toLocalTime();

        DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String currentDateString = currentDate.format(dtfDate);
        String currentTimeString = currentTime.format(dtfTime);

        List<Event> potentialEventsToAlert = this.eventRepository.getEventsToAlert(userId, currentDateString, currentTimeString);

        List<Event> eventsToAlert = new ArrayList<>();
        for (Event event: potentialEventsToAlert) {
            if(event.getAlertDate().isEqual(currentDate)) {
                eventsToAlert.add(event);
            } else {
                DateTime alertJodaDate = new DateTime().withDate(event.getAlertDate().getYear(), event.getAlertDate().getMonthValue(),
                        event.getAlertDate().getDayOfMonth());
                DateTime startJodaDate = new DateTime().withDate(event.getStartDate().getYear(), event.getStartDate().getMonthValue(),
                        event.getStartDate().getDayOfMonth());
                int daysBetweenAlertAndStartDate = Days.daysBetween(startJodaDate, alertJodaDate).getDays();

                if(repetitionService.checkDate(event.getId(), currentDate.plusDays(daysBetweenAlertAndStartDate))) {
                    event = setEventsDates(event, currentDate);
                    eventsToAlert.add(event);
                }
            }
        }

        return eventsToAlert;
    }

    public Event getEventByRepetitionId(long repetitionId) {
        return this.eventRepository.getEventByRepetitionId(repetitionId);
    }

    /**
     * Update event
     *
     * @param event   event object which is going to be updated
     * @param eventId id of Event which is going to be updated
     */
    public void update(long eventId, Event event) {
        this.eventRepository.update(eventId, event);
    }

    @Transactional
    public void updateEventInRepetitionAtDate(long userId, long repetitionId, Event event, String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        Long newEventId = this.eventRepository.add(event, userId);
        repetitionService.addException(repetitionId, newEventId, date);
    }

    public void updateAllEventsInRepetition(long repetitionId, Event updatedEvent) {
        Event eventFromDb = eventRepository.getEventByRepetitionId(repetitionId);

        LocalDateTime start = LocalDateTime.of(eventFromDb.getStartDate(), updatedEvent.getStartTime());
        LocalDateTime end = LocalDateTime.of(eventFromDb.getEndDate(), updatedEvent.getEndTime());
        LocalDateTime alert = LocalDateTime.of(eventFromDb.getAlertDate(), updatedEvent.getAlertTime());

        if(end.isBefore(start)) {
            end = end.plusDays(1);
        }

        if(alert.isAfter(start)) {
            alert = alert.minusDays(1);
        }

        updatedEvent.setEndDate(end.toLocalDate());
        updatedEvent.setAlertDate(alert.toLocalDate());
        updatedEvent.setStartDate(start.toLocalDate());

        eventRepository.update(eventFromDb.getId(), updatedEvent);
    }

    @Transactional
    public void updateRepetition(long id, Repetition repetition) {
        this.repetitionService.deleteExceptionsByRepetitionId(id);
        this.repetitionService.update(id, repetition);

        // set new valid dates to event
        Event event = eventRepository.getEventByRepetitionId(id);
        LocalDate newStartDate = this.repetitionService.validateStart(repetition);
        event = setEventsDates(event, newStartDate);

        this.eventRepository.update(event.getId(), event);
    }

    @Transactional
    public void postponeEvent(long userId, long eventId, Event postponedEvent, Event originalEvent) {
        Repetition repetition = this.repetitionService.getRepetitionByEventId(eventId);
        if(repetition == null) {
            this.eventRepository.postpone(eventId, postponedEvent);
        } else {
            Event exceptionalEvent = originalEvent;
            exceptionalEvent.setAlertDate(postponedEvent.getAlertDate());
            exceptionalEvent.setAlertTime(postponedEvent.getAlertTime());
            Long newEventId = this.eventRepository.add(exceptionalEvent, userId);
            repetitionService.addException(repetition.getId(), newEventId, postponedEvent.getStartDate());
        }
    }

    public void delete(long idEvent) {
        this.eventRepository.delete(idEvent);
    }


    public void deleteFromRepetition(long repetitionId, String dateString) {
        // TODO check if date is valid in repetition
        LocalDate date = LocalDate.parse(dateString);
        Exception exception = repetitionService.getExceptionByRepetitionIdAndDate(repetitionId, date);

        // if event isn't exception in repetition add new exception
        if (exception == null) {
            this.repetitionService.addException(repetitionId, date);
        }
    }

    private Event setEventsDates(Event event, LocalDate newStart) {
        LocalDate start = event.getStartDate();
        LocalDate end = event.getEndDate();
        LocalDate alert = event.getAlertDate();

        event.setStartDate(newStart);
        event.setEndDate(countEndDate(start, end, newStart));
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
