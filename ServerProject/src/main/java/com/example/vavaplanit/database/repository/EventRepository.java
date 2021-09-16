package com.example.vavaplanit.database.repository;

import com.example.vavaplanit.database.mappers.EventMapper;
import com.example.vavaplanit.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;


@Repository
public class EventRepository {

    private final JdbcTemplate jdbcTemplate;
    private final EventMapper eventMapper;

    @Autowired
    public EventRepository(JdbcTemplate jdbcTemplate, EventMapper eventMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.eventMapper = eventMapper;
    }


    public Long add(Event event, long userId) {
        final String sql = "insert into events (title, location, type, description, start_date, start_time, end_date, end_time," +
                " alert_date, alert_time, author_id) values (?,?,?,?,?,?,?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, event.getTitle());
                if (event.getLocation() != null) {
                    ps.setString(2, event.getLocation());
                } else {
                    ps.setNull(2, Types.VARCHAR);
                }

                ps.setString(3, event.getType().toString());

                if (event.getDescription() != null) {
                    ps.setString(4, event.getDescription());
                } else {
                    ps.setNull(4, Types.VARCHAR);
                }
                ps.setDate(5, Date.valueOf(event.getStartDate()));
                ps.setTime(6, Time.valueOf(event.getStartTime()));
                ps.setDate(7, Date.valueOf(event.getEndDate()));
                ps.setTime(8, Time.valueOf(event.getEndTime()));
                ps.setDate(9, Date.valueOf(event.getAlertDate()));
                ps.setTime(10, Time.valueOf(event.getAlertTime()));
                ps.setLong(11, userId);

                return ps;
            }
        }, keyHolder);

        if (keyHolder.getKeys() != null) {
            return (Long) keyHolder.getKeys().get("id");
        } else {
            return null;
        }
    }

    public List<Event> getEventsByDate(long idUser, LocalDate date) {
        String sql = "SELECT id, title, type, start_date, start_time FROM events WHERE author_id = " + idUser + " " +
                "AND start_date <= '" + date + "' ORDER BY start_time ASC;";
        return jdbcTemplate.query(sql, eventMapper.mapBasicEventInfoFromDb());
    }

    public List<Event> getEventsByMonthAndUserId(long userId, LocalDate minDate, LocalDate maxDate) {
        String sql = "SELECT id, title, type, start_date, start_time FROM events WHERE author_id = " + userId + " AND start_date < '" + maxDate +
                "' ORDER BY start_time;";
        return jdbcTemplate.query(sql, eventMapper.mapBasicEventInfoFromDb());
    }

    public Event getEvent(long eventId) {
        try {
            String sql = "SELECT * FROM events WHERE id = " + eventId;
            return jdbcTemplate.queryForObject(sql, eventMapper.mapEventFromDb());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    /**
     * Getting all events of user that have notifications set for a given time and date
     *
     * @param userId ID of user
     * @param currentDate   selected date
     * @param currentTime   selected time
     */
    public List<Event> getEventsToAlert(long userId, String currentDate, String currentTime) {
        String sql = "SELECT * FROM events WHERE alert_date = '" + currentDate + "' AND alert_time = '" + currentTime +
                "' AND author_id = " + userId + ";";
        return jdbcTemplate.query(sql, eventMapper.mapEventFromDb());
    }

    /**
     * Update event
     *
     * @param event event object with updated attributes
     * @param id    id of Event which is going to be updated
     */
    public void update(long id, Event event) {
        String sql = "UPDATE events SET title = ?, location = ?, type = ?, start_date = ?, start_time = ?, end_date = ?, end_time = ?," +
                " alert_date = ?, alert_time = ?, description = ? WHERE id = ?";
        jdbcTemplate.update(sql, event.getTitle(), event.getLocation(), event.getType().toString(), event.getStartDate(),
                event.getStartTime(), event.getEndDate(), event.getEndTime(), event.getAlertDate(), event.getAlertTime(), event.getDescription(), id);
    }

    /**
     * Delete event by event's id
     *
     * @param id ID of Event which is going to be deleted
     */
    public void delete(long id) {
        String sql = "DELETE FROM events WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
