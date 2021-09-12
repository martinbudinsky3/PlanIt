package com.example.vavaplanit.database.mappers;

import com.example.vavaplanit.model.Event;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDate;
import java.time.LocalTime;

// TODO without anonymous methods
/** Class for mapping Event object. */
public class EventMapper {
    public RowMapper<Event> mapEventFromDb() {
        return (resultSet, i) -> {
            int idEvent = resultSet.getInt("idevent");
            String title = resultSet.getString("title");
            String location = resultSet.getString("location");
            Event.Type type = Event.Type.fromString(resultSet.getString("type"));
            String description = resultSet.getString("description");
            LocalDate date = resultSet.getObject("date", LocalDate.class);
            LocalTime starts = resultSet.getObject("starts", LocalTime.class);
            LocalDate endsDate = resultSet.getObject("ends_date", LocalDate.class);
            LocalTime ends = resultSet.getObject("ends", LocalTime.class);
            LocalDate alertDate = resultSet.getObject("alert_date", LocalDate.class);
            LocalTime alert = resultSet.getObject("alert", LocalTime.class);
            Integer exceptionId = resultSet.getInt("exception_id");
            return new Event(
                    idEvent,
                    exceptionId,
                    title,
                    location,
                    type,
                    description,
                    date,
                    starts,
                    endsDate,
                    ends,
                    alertDate,
                    alert
            );
        };
    }

    public RowMapper<Event> mapBasicEventInfoFromDb() {
        return (resultSet, i) -> {
            int idEvent = resultSet.getInt("idevent");
            String title = resultSet.getString("title");
            Event.Type type = Event.Type.fromString(resultSet.getString("type"));
            LocalDate date = resultSet.getObject("date", LocalDate.class);
            LocalTime starts = resultSet.getObject("starts", LocalTime.class);

            return new Event(
                    idEvent,
                    title,
                    type,
                    date,
                    starts
            );
        };
    }
}
