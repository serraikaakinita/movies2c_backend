package com.movies2c.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PostRatings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRating {

    @Id
    private String id;

    private String postId;

    private String userId;
    private int value; // 1..5

    private long createdAt;
}
