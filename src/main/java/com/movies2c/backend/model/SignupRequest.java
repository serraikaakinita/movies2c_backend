package com.movies2c.backend.model;

public class SignupRequest {

    private String userName;
    private String email;
    private String password;

    public String getUserName(){
        return userName;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }
}
