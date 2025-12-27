package com.movies2c.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto{
    private String id;
    private String userName;
    private String email;
    private String bio;
    private long date;
}