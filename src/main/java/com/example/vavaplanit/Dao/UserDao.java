package com.example.vavaplanit.Dao;

import com.example.vavaplanit.Model.User;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserDao {

    private static Map<Integer, User> users;

    static {
        users = new HashMap<Integer, User>(){
            {
                put(1, new User(1, "Ivana", "Balekova"));
                put(2, new User (2, "Martin", "Budinsky"));
            }
        };
    }

    public Collection<User> getAllUsers(){
        return this.users.values();
    }
}
