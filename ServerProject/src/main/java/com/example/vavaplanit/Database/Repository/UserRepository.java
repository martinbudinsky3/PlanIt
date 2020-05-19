package com.example.vavaplanit.Database.Repository;

import com.example.vavaplanit.Database.Mappers.UserMappers;
import com.example.vavaplanit.Model.Event;
import com.example.vavaplanit.Model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

/** Communication with database. (queries relating to the User object)*/
@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserMappers userMappers = new UserMappers();

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Inserting new user into DB
     * @param  user User objedct which is going to be inserted*/
    public Integer add(User user) {

        final String sql = "INSERT INTO planitschema.user (firstName, lastName, userName, userPassword) " +
                    " SELECT '" + user.getFirstName() + "', '" + user.getLastName() + "', '" + user.getUserName() + "', '" + user.getUserPassword() + "' " +
                    " WHERE NOT EXISTS (SELECT * FROM planitschema.user WHERE username='" + user.getUserName() + "' " +
                    " and userpassword = '" + user.getUserPassword() + "');";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                return ps;
            }
        }, keyHolder);

        if(keyHolder.getKeys() != null) {
            return (Integer) keyHolder.getKeys().get("iduser");
        } else {
            return null;
        }
    }

    /**
     * Used to login
     * @param userName username of user
     * @param userPassword password of user*/
    public User getUserByUserNameAndUserPassword(String userName, String userPassword){
        try {
            String sql = "SELECT * FROM planitschema.user " +
                    " where username = '" + userName + "' and userpassword = '" + userPassword + "';";
            return jdbcTemplate.queryForObject(sql, userMappers.mapUserFomDb());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


}
