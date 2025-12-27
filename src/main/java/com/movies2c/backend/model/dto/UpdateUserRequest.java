package com.movies2c.backend.model.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String userName;
    private String email;

    private String oldPassword;
    private String newPassword;

    private String bio;
}