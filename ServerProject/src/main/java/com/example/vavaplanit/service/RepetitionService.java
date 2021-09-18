package com.example.vavaplanit.service;

import com.example.vavaplanit.database.repository.ExceptionRepository;
import com.example.vavaplanit.database.repository.RepetitionRepository;
import com.example.vavaplanit.model.Exception;
import com.example.vavaplanit.model.repetition.MonthlyRepetition;
import com.example.vavaplanit.model.repetition.Repetition;
import com.example.vavaplanit.model.repetition.WeeklyRepetition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepetitionService {
    @Autowired
    private RepetitionRepository repetitionRepository;
    @Autowired
    private ExceptionRepository exceptionRepository;

    public Long addRepetition(Repetition repetition) {
        return repetitionRepository.add(repetition);
    }

    public Long addException(long repetitionId, LocalDate exceptionDate) {
        Exception exception = new Exception(exceptionDate, repetitionId);

        return exceptionRepository.add(exception);
    }

    public Long addException(long repetitionId, long eventId, LocalDate exceptionDate) {
        Exception exception = new Exception(exceptionDate, repetitionId, eventId);

        return exceptionRepository.add(exception);
    }

    public Exception getExceptionByEventId(long eventId) {
        return null;
    }

    public Repetition getRepetitionById(long repetitionId) {
        return repetitionRepository.getRepetitionById(repetitionId);
    }

    public void deleteExceptionsByRepetitionId(long repetitionId) {
        this.repetitionRepository.deleteExceptionsByRepetitionId(repetitionId);
    }

    public List<LocalDate> getEventDates(long eventId, int month, int year) {
        Repetition repetition = this.repetitionRepository.getRepetitionByEventId(eventId);

        LocalDate minDate = LocalDate.of(year, month, 1);
        LocalDate maxDate = minDate.plusMonths(1).minusDays(1);

        if(repetition == null || minDate.isAfter(repetition.getEnd()) || maxDate.isBefore(repetition.getStart())) {
            return new ArrayList<>();
        }

        List<Exception> exceptions = this.exceptionRepository.getExceptionsDates(repetition.getEventId());
        List<LocalDate> exceptionsDates = new ArrayList<>();
        exceptions.forEach(exception -> exceptionsDates.add(exception.getDate()));

        return repetition.figureOutDates(month, year).stream().filter(date -> !exceptionsDates.contains(date))
                .collect(Collectors.toList());
    }

    public void update(Repetition repetition) {
        this.repetitionRepository.update(repetition);
    }

    public boolean checkDate(long eventId, LocalDate date) {
        Repetition repetition = this.repetitionRepository.getRepetitionByEventId(eventId);
        if(repetition == null) {
            return false;
        }

        Exception exception = this.getExceptionByRepetitionIdAndDate(repetition.getId(), date);
        if(exception != null) {
            return false;
        }

        return repetition.checkDate(date);
    }

    public LocalDate validateStart(Repetition repetition) {
        if(repetition instanceof WeeklyRepetition) {
            ((WeeklyRepetition) repetition).validateStart();
        }

        return repetition.getStart();
    }

    public Exception getExceptionByRepetitionIdAndDate(long repetitionId, LocalDate date) {
        return exceptionRepository.getExceptionByRepetitionIdAndDate(repetitionId, date);
    }

    public Repetition getRepetitionByEventId(long eventId) {
        return repetitionRepository.getRepetitionByEventId(eventId);
    }
}
