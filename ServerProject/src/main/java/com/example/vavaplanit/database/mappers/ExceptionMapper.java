package com.example.vavaplanit.database.mappers;

import com.example.vavaplanit.model.Exception;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ExceptionMapper {

    public RowMapper<Exception> mapDate() {
        return (resultSet, i) -> {
            LocalDate date = resultSet.getObject("date", LocalDate.class);

            return new Exception(date);
        };
    }

    public RowMapper<Exception> mapException() {
        return (resultSet, i) -> {
            LocalDate date = resultSet.getObject("date", LocalDate.class);
            Long eventId = resultSet.getLong("event_id");
            long repetitionId = resultSet.getLong("repetition_id");

            return new Exception(date, repetitionId, eventId);
        };
    }
}
