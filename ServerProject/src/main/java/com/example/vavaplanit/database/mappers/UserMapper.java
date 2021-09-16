package com.example.vavaplanit.database.mappers;

import com.example.vavaplanit.model.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public RowMapper<User> mapUserFomDb() {
        return (resultSet, i) -> {
            long id = resultSet.getLong("id");
            String firstName = resultSet.getString("firstname");
            String lastName = resultSet.getString("lastname");
            String userName = resultSet.getString("username");
            String userPassword = resultSet.getString("password");

            return new User(
                    id,
                    firstName,
                    lastName,
                    userName,
                    userPassword
            );
        };
    }
}
