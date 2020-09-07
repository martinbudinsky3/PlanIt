package com.example.vavaplanit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class User implements Serializable {
    private int idUser;
    private String firstName;
    private String lastName;
    private String userName;
    @JsonIgnore
    private String userPassword;

    public User(int idUser, String firstName, String lastName, String userName, String userPassword) {
        this.idUser = idUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
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
