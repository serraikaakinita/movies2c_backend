package com.movies2c.backend.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document (collection = "UserFavoritesMovies")
public class UserFavoriteMovies {
    @Id

    private  String id;
    private String userId;
    private Long movieId;
    private String title;
    private String posterPath;
    private String overview;
    private String releaseDate;
    private String voteAverage;
    private  Long createdAt;
}
