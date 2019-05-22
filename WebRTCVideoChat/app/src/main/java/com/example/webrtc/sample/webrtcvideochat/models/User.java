package com.example.webrtc.sample.webrtcvideochat.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private int age;
    private String email;
    private boolean isLogin;
    public User() {
    }

    public User( String username, String password, String firstname, String lastname, int age, String email,boolean isLogin) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.email = email;
        this.isLogin=isLogin;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public boolean isLogin() {
        return isLogin;
    }
}
