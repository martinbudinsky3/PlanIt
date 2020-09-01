package com.example.vavaplanit.service;

import com.example.vavaplanit.database.repository.ExceptionRepository;
import com.example.vavaplanit.database.repository.RepetitionRepository;
import com.example.vavaplanit.model.Exception;
import com.example.vavaplanit.model.repetition.Repetition;
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

    public Long add(Repetition repetition) {
        return repetitionRepository.add(repetition);
    }

    public Repetition getRepetitionByEventIdOrExceptionId(long eventId, Long exceptionId) {
        return this.repetitionRepository.getRepetitionByEventIdOrExceptionId(eventId, exceptionId);
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
}
