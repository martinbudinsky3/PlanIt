package com.example.vavaplanit.database.repository;

import com.example.vavaplanit.database.mappers.RepetitionMapper;
import com.example.vavaplanit.model.Event;
import com.example.vavaplanit.model.repetition.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class RepetitionRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RepetitionMapper repetitionInfoMapper = new RepetitionMapper();

    @Autowired
    public RepetitionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer add(Repetition repetition) {
        final String sql = "insert into planitschema.repetition (starts, ends, repeat_interval, days_of_week, " +
                "day_of_month, repeat_ordinal, month, type, event_id) values (?,?,?,?,?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(getPreparedStatementCreator(repetition, sql), keyHolder);

        if (keyHolder.getKeys() != null) {
            return (Integer) keyHolder.getKeys().get("event_id");
        } else {
            return null;
        }
    }

    public Repetition getRepetitionByEventId(int eventId) {
        String sql = "SELECT planitschema.repetition.* FROM planitschema.repetition WHERE event_id = " + eventId + " LIMIT 1;";

        try {
            return jdbcTemplate.queryForObject(sql, repetitionInfoMapper);
        } catch (IncorrectResultSizeDataAccessException exception){
            return null;
        }
    }

    public Repetition getRepetitionByEventIdOrExceptionId(int eventId, Integer exception_id) /*throws EmptyResultDataAccessException*/ {
        String sql = "SELECT planitschema.repetition.* FROM planitschema.repetition r JOIN planitschema.exception e WHERE r.event_id = " + eventId +
                " OR e.exception_id = " + exception_id + " LIMIT 1;";

        try {
            return jdbcTemplate.queryForObject(sql, repetitionInfoMapper);
        } catch(IncorrectResultSizeDataAccessException exception) {
            return null;
        }
    }

    public void update(Repetition repetition) {
        String sql = "UPDATE planitschema.repetition SET starts = ?, ends = ?, repeat_interval = ?, days_of_week = ?, " +
                "day_of_month = ?, repeat_ordinal = ?, month = ?, type = ? WHERE event_id = ?";

        jdbcTemplate.update(sql, jdbcTemplate.update(getPreparedStatementCreator(repetition, sql))
        );
    }

    private PreparedStatementCreator getPreparedStatementCreator(Repetition repetition, String sql) {
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

            ps.setLong(9, repetition.getEventId());

            return ps;
        };
    }
}
