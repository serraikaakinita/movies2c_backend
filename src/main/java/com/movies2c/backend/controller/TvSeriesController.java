package com.movies2c.backend.controller;

import com.movies2c.backend.service.TmdbClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class TvSeriesController {

    private final TmdbClient tmdbClient;

    public TvSeriesController(TmdbClient tmdbClient){
        this.tmdbClient=tmdbClient;
    }

    @GetMapping("/api/tvseries/trending")
    public String trendingTvseries(){return this.tmdbClient.getTrendingTvSeries();}

    @GetMapping("/api/tv/genre")
    public String getTvShowsByGenre(@RequestParam String genreId) {
        return tmdbClient.getTvSeriesByGenres(genreId);
    }

    @GetMapping("/api/tv")
    public String getTvDetails(@RequestParam String id) {
        return tmdbClient.getTvSeriesDetails(id);
    }
    @GetMapping("/api/search/tv")
    public String searchTvByName(@RequestParam String name) {
        return tmdbClient.searchTvSeriesByName(name);
    }
    
}
