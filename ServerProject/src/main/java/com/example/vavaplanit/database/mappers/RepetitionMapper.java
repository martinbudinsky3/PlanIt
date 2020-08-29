package com.example.vavaplanit.database.mappers;

import com.example.vavaplanit.model.repetition.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class RepetitionMapper implements RowMapper<Repetition> {
    @Override
    public Repetition mapRow(ResultSet resultSet, int i) throws SQLException {
        Long eventId = resultSet.getLong("event_id");
        LocalDate start = resultSet.getObject("starts", LocalDate.class);
        LocalDate end = resultSet.getObject("ends", LocalDate.class);
        int repeatInterval = resultSet.getInt("repeat_interval");
        int daysOfWeek = resultSet.getInt("days_of_week");
        int dayOfMonth = resultSet.getInt("day_of_month");
        int repeatOrdinal = resultSet.getInt("repeat_ordinal");
        int month = resultSet.getInt("month");
        RepetitionType type = RepetitionType.fromString(resultSet.getString("type"));

        if (type == RepetitionType.DAILY) {
            return new Repetition(eventId, start, end, repeatInterval);
        }

        if (type == RepetitionType.WEEKLY) {
            return new WeeklyRepetition(eventId, start, end, repeatInterval, daysOfWeek);
        }

        if (type == RepetitionType.MONTHLY) {
            return new MonthlyRepetition(eventId, start, end, repeatInterval, dayOfMonth, daysOfWeek, repeatOrdinal);
        }

        if (type == RepetitionType.YEARLY) {
            return new YearlyRepetition(eventId, start, end, repeatInterval, dayOfMonth, daysOfWeek, repeatOrdinal, month);
        }

        return null;
    }
}
