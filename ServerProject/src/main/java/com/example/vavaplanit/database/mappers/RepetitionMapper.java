package com.example.vavaplanit.database.mappers;

import com.example.vavaplanit.model.repetition.*;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RepetitionMapper {

    public RowMapper<Repetition> mapRepetition() {
        return (resultSet, i) -> {
            long eventId = resultSet.getLong("event_id");
            LocalDate start = resultSet.getObject("start", LocalDate.class);
            LocalDate end = resultSet.getObject("end", LocalDate.class);
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
                return new MonthlyRepetition(eventId, start, end, repeatInterval, daysOfWeek, dayOfMonth, repeatOrdinal);
            }

            if (type == RepetitionType.YEARLY) {
                return new YearlyRepetition(eventId, start, end, repeatInterval, daysOfWeek, dayOfMonth, repeatOrdinal, month);
            }

            return null;
        };
    }
}
