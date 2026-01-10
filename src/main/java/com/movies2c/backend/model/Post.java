package com.movies2c.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    private String id;

    private String movieId;

    private String userId;
    private String userName;

    private String mediaType; //photo h vid tha mpainei
    private String mediaId;

    private String caption;

    private long createdAt;

    private double avgRating;
    private int ratingCount;
}
