package com.movies2c.backend.model.dto;


public class ChatRequest {
    public String message;
    public MovieContext movie; // προαιρετικό
    public static class MovieContext {
        public String title;
        public String overview;
        public String release_date;
        public Double vote_average;
    }
}
