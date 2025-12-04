package com.movies2c.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("UserToken")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserToken {
    @Id
    private String id;
    private String userId;
    private String token;
    @Indexed(name = "expireAfterOneHour",expireAfterSeconds = 60)
    private Date created_at;
    //constractor
    public UserToken(String token,String userId) {
        this.token = token;
        this.userId =userId;
        this.created_at = new Date();
    }

}
