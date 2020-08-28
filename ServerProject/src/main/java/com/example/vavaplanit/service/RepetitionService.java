package com.example.vavaplanit.service;

import com.example.vavaplanit.database.repository.RepetitionRepository;
import com.example.vavaplanit.model.repetition.Repetition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class RepetitionService {
    @Autowired
    private RepetitionRepository repetitionRepository;

    public Long add(Repetition repetition) {
        return repetitionRepository.add(repetition);
    }

    public Repetition getRepetitionByEventId(int eventId) {
        return repetitionRepository.getRepetitionByEventId(eventId);
    }

    public List<LocalDate> getEventDates(int eventId, int month, int year) {
        Repetition repetition = getRepetitionByEventId(eventId);

        LocalDate minDate = LocalDate.of(year, month, 1);
        LocalDate maxDate = minDate.plusMonths(1).minusDays(1);

        if(repetition == null || minDate.isAfter(repetition.getEnd()) || maxDate.isBefore(repetition.getStart())) {
            return null;
        }

        return repetition.figureOutDates(month, year);
    }
}
