package com.example.vavaplanit.Database.Mappers;

import com.example.vavaplanit.Model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UserMappers {

    public RowMapper<User> mapUserFomDb() {
        return (resultSet, i) -> {
            int idUser = resultSet.getInt("idUser");
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            String userName = resultSet.getString("userName");
            String userPassword = resultSet.getString("userPassword");

            return new User(
                    idUser,
                    firstName,
                    lastName,
                    userName,
                    userPassword
            );
        };
    }
}
