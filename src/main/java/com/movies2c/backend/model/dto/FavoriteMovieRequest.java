package com.movies2c.backend.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class FavoriteMovieRequest {
    private Long movieId;
    private String title;
    private String posterPath;
    private String overview;
    private String releaseDate;
    private String voteAverage;
}
