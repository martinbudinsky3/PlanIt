package com.example.vavaplanit.database.mappers;

import com.example.vavaplanit.model.Exception;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ExceptionDateMapper implements RowMapper<Exception> {
    @Override
    public Exception mapRow(ResultSet resultSet, int i) throws SQLException {
        LocalDate date = resultSet.getObject("date", LocalDate.class);

        return new Exception(date);
    }
}
