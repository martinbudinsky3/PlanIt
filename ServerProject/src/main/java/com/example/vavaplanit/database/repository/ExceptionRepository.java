package com.example.vavaplanit.database.repository;

import com.example.vavaplanit.database.mappers.ExceptionMapper;
import com.example.vavaplanit.model.Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ExceptionRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ExceptionMapper exceptionMapper;

    @Autowired
    public ExceptionRepository(JdbcTemplate jdbcTemplate, ExceptionMapper exceptionMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.exceptionMapper = exceptionMapper;
    }

    public Long add(Exception exception) {
        final String sql = "insert into exceptions (date, repetition_id, event_id) values (?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setDate(1, Date.valueOf(exception.getDate()));
                ps.setLong(2, exception.getRepetitionId());
                if(exception.getEventId() != null) {
                    ps.setLong(3, exception.getEventId());
                } else {
                    ps.setNull(3, Types.BIGINT);
                }
                return ps;
            }
        }, keyHolder);

        if (keyHolder.getKeys() != null) {
            return (Long) keyHolder.getKeys().get("id");
        } else {
            return null;
        }
    }

    public List<Exception> getExceptionsDatesByRepetitionId(long repetitionId) {
        String sql = "SELECT e.date FROM exceptions e WHERE repetition_id = ?;";

        return jdbcTemplate.query(sql, new Object[]{repetitionId}, exceptionMapper.mapDate());
    }

    public Exception getExceptionByRepetitionIdAndDate(long repetitionId, LocalDate date) {
        String sql = "SELECT * FROM exceptions e WHERE repetition_id = ? AND e.date = ?;";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{repetitionId, date}, exceptionMapper.mapException());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }
}
