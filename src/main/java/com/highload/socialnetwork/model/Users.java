package com.highload.socialnetwork.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class Users implements Serializable {
    private String id;
    private String login;
    private String firstName;
    private String secondName;
    private LocalDate birthdate;
    private String biography;
    private String city;
    private String password;
}
