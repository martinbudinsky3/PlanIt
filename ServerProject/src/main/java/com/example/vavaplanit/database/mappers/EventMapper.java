package com.example.vavaplanit.database.mappers;

import com.example.vavaplanit.model.Event;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class EventMapper {
    public RowMapper<Event> mapEventFromDb() {
        return (resultSet, i) -> {
            long id = resultSet.getLong("id");
            long authorId = resultSet.getLong("author_id");
            String title = resultSet.getString("title");
            String location = resultSet.getString("location");
            Event.Type type = Event.Type.fromString(resultSet.getString("type"));
            String description = resultSet.getString("description");
            LocalDate startDate = resultSet.getObject("start_date", LocalDate.class);
            LocalTime startTime = resultSet.getObject("start_time", LocalTime.class);
            LocalDate endDate = resultSet.getObject("end_date", LocalDate.class);
            LocalTime endTime = resultSet.getObject("end_time", LocalTime.class);
            LocalDate alertDate = resultSet.getObject("alert_date", LocalDate.class);
            LocalTime alertTime = resultSet.getObject("alert_time", LocalTime.class);
            return new Event(
                    id,
                    authorId,
                    title,
                    location,
                    type,
                    description,
                    startDate,
                    startTime,
                    endDate,
                    endTime,
                    alertDate,
                    alertTime
            );
        };
    }

    public RowMapper<Event> mapBasicEventInfoFromDb() {
        return (resultSet, i) -> {
            int idEvent = resultSet.getInt("id");
            String title = resultSet.getString("title");
            Event.Type type = Event.Type.fromString(resultSet.getString("type"));
            LocalDate startDate = resultSet.getObject("start_date", LocalDate.class);
            LocalTime startTime = resultSet.getObject("start_time", LocalTime.class);

            return new Event(
                    idEvent,
                    title,
                    type,
                    startDate,
                    startTime
            );
        };
    }
}
