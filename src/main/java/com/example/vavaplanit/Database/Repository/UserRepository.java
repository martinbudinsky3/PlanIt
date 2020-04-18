package com.example.vavaplanit.Database.Repository;

import com.example.vavaplanit.Database.Mappers.UserMappers;
import com.example.vavaplanit.Model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;
    UserMappers userMappers = new UserMappers();

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> getAllUsers() {
        String sql = "" +
                "SELECT " +
                " idUser, " +
                " firstName, " +
                " lastName " +
                "FROM planitschema.user";

        return jdbcTemplate.query(sql, userMappers.mapUserFomDb());
    }

//    public User getUserById(int idUser){
//        return this.users.get(idUser);
//    }


}
