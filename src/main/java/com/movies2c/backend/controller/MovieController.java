package com.movies2c.backend.controller;

import com.movies2c.backend.service.TmdbClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class
MovieController {
    private final TmdbClient tmdbClient;

    public MovieController(TmdbClient tmdbClient) {
        this.tmdbClient = tmdbClient;
    }

    //fetching trending movies
    @GetMapping("/api/movies/trending")
    public String trendingMovies() {
        return tmdbClient.getTrendingMovies();
    }


    @GetMapping("/api/movies/genre")
    public String getMoviesByGenre(@RequestParam String genreId) {
        return tmdbClient.getMoviesByGenres(genreId);
    }
    @GetMapping("/api/movie")
    public String getMovieDetails(@RequestParam(required = true) String id) {
        return this.tmdbClient.getMovieDetails(id);

    }

    @GetMapping("/api/search/movie")
    public String searchMovieByName(@RequestParam(required = true) String name){
        return  this.tmdbClient.searchMovieByName(name);
    }
    @GetMapping("/api/movie/cast")
    public String getCastDetails(@RequestParam(required = true) String id) {
        return this.tmdbClient.getCastDetails(id);
    }

    @GetMapping("/api/movie/trailer")
    public String getMovieTrailer(@RequestParam String id) {
        return tmdbClient.getMovieTrailer(id);
    }
}
