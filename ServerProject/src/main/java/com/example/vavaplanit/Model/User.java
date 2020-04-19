package com.example.vavaplanit.Model;

import javax.persistence.Column;
import java.io.Serializable;

public class User implements Serializable {
    private int idUser;
    private String firstName;
    private String lastName;

    public User(int idUser, String firstName, String lastName) {
        this.idUser = idUser;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
