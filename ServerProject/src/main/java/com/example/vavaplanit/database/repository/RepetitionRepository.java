package com.example.vavaplanit.database.repository;

import com.example.vavaplanit.database.mappers.RepetitionMapper;
import com.example.vavaplanit.model.repetition.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class RepetitionRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RepetitionMapper repetitionMapper;

    @Autowired
    public RepetitionRepository(JdbcTemplate jdbcTemplate, RepetitionMapper repetitionMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.repetitionMapper = repetitionMapper;
    }

    public Long add(Repetition repetition) {
        final String sql = "insert into repetitions (start, \"end\", repeat_interval, days_of_week, " +
                "day_of_month, repeat_ordinal, month, type, event_id) values (?,?,?,?,?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(getPreparedStatementCreator(repetition, sql, true), keyHolder);

        if (keyHolder.getKeys() != null) {
            return (Long) keyHolder.getKeys().get("id");
        } else {
            return null;
        }
    }

    public Repetition getRepetitionByEventId(long eventId) {
        String sql = "SELECT * FROM repetitions WHERE event_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[] {eventId}, repetitionMapper.mapRepetition());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    public Repetition getRepetitionByEventIdViaException(long eventId) {
        String sql = "SELECT r.* FROM events ev JOIN exceptions ex ON ev.id = ex.event_id JOIN repetitions r ON ex.repetition_id = r.id" +
                " WHERE ex.event_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[] {eventId}, repetitionMapper.mapRepetition());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    public Repetition getRepetitionById(long repetitionId) {
        String sql = "SELECT * FROM repetitions WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[] {repetitionId}, repetitionMapper.mapRepetition());
        } catch (EmptyResultDataAccessException exception){
            return null;
        }
    }

    public void update(Repetition repetition) {
        String sql = "UPDATE repetitions SET start = ?, \"end\" = ?, repeat_interval = ?, days_of_week = ?, " +
                "day_of_month = ?, repeat_ordinal = ?, month = ?, type = ? WHERE id = ?";

        jdbcTemplate.update(getPreparedStatementCreator(repetition, sql, false));
    }

    public void deleteExceptionsByRepetitionId(long repetitionId) {
        String sql = "DELETE FROM exceptions WHERE repetition_id = ?";

        jdbcTemplate.update(sql, repetitionId);
    }

    private PreparedStatementCreator getPreparedStatementCreator(Repetition repetition, String sql, boolean create) {
        return connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setDate(1, Date.valueOf(repetition.getStart()));
            ps.setDate(2, Date.valueOf(repetition.getEnd()));
            ps.setInt(3, repetition.getRepetitionInterval());
            if (repetition instanceof WeeklyRepetition && ((WeeklyRepetition) repetition).getDaysOfWeek() != null) {
                ps.setInt(4, ((WeeklyRepetition) repetition).getDaysOfWeekInt());
            } else {
                ps.setNull(4, Types.INTEGER);
            }

            if (repetition instanceof MonthlyRepetition && ((MonthlyRepetition) repetition).getDayOfMonth() != null) {
                ps.setInt(5, ((MonthlyRepetition) repetition).getDayOfMonth());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            if (repetition instanceof MonthlyRepetition && ((MonthlyRepetition) repetition).getOrdinal() != null) {
                ps.setInt(6, ((MonthlyRepetition) repetition).getOrdinal());
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            if (repetition instanceof YearlyRepetition) {
                ps.setInt(7, ((YearlyRepetition) repetition).getMonth());
            } else {
                ps.setNull(7, Types.INTEGER);
            }

            if (repetition.getClass() == Repetition.class) {
                ps.setString(8, RepetitionType.DAILY.toString());
            } else if (repetition.getClass() == WeeklyRepetition.class) {
                ps.setString(8, RepetitionType.WEEKLY.toString());
            } else if (repetition.getClass() == MonthlyRepetition.class) {
                ps.setString(8, RepetitionType.MONTHLY.toString());
            } else {
                ps.setString(8, RepetitionType.YEARLY.toString());
            }

            if(create) {
                ps.setLong(9, repetition.getEventId());
            } else {
                ps.setLong(9, repetition.getId());
            }

            return ps;
        };
    }
}
