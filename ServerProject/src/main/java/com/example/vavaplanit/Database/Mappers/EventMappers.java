package com.example.vavaplanit.Database.Mappers;

import com.example.vavaplanit.Model.Event;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDate;
import java.time.LocalTime;

/** Class for mapping Event object. */
public class EventMappers {
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
            return new Event(
                    idEvent,
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
}
