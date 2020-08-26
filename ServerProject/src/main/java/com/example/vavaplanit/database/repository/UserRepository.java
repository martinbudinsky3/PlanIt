package com.example.vavaplanit.database.repository;

import com.example.vavaplanit.database.mappers.UserMapper;
import com.example.vavaplanit.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;

/** Communication with database. (queries relating to the User object)*/
@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMappers = new UserMapper();

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Inserting new user into DB
     * @param  user User objedct which is going to be inserted*/
    public Integer add(User user) {

        final String sql = "INSERT INTO planitschema.user (firstName, lastName, userName, userPassword) " +
                    "values (?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getUserName());
            ps.setString(4, user.getUserPassword());

            return ps;
        }, keyHolder);

        if(keyHolder.getKeys() != null) {
            return (Integer) keyHolder.getKeys().get("iduser");
        } else {
            return null;
        }
    }

//    /**
//     * Used to login
//     * @param username username of user
//     * @param password password of user*/
//    public User getUserByUsernameAndPassword(String username, String password){
//        try {
//            String sql = "SELECT * FROM planitschema.user " +
//                    " where username = '" + username + "' and userpassword = '" + password + "';";
//            return jdbcTemplate.queryForObject(sql, userMappers.mapUserFomDb());
//        } catch (EmptyResultDataAccessException e) {
//            return null;
//        }
//    }

    public User getUserByUsername(String username){
        try {
            String sql = "SELECT * FROM planitschema.user " +
                    " where username = '" + username + "';";
            return jdbcTemplate.queryForObject(sql, userMappers.mapUserFomDb());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean getHashed(String username) {
        String sql = "SELECT hashed FROM planitschema.user " +
                " where username = '" + username + "';";
        return jdbcTemplate.queryForObject(sql, Boolean.class);
    }

    public void updateHashed(String username, String hashedPassword) {
        String sql = "UPDATE planitschema.user set hashed = ?, userpassword = ?" +
                " where username = ?;";
        jdbcTemplate.update(sql, true, hashedPassword, username);
    }
}
