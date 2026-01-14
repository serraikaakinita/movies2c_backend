package com.movies2c.backend.service;

import com.movies2c.backend.model.dto.ChatRequest;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import org.springframework.stereotype.Service;

@Service
public class OpenAIChatService {

    private final OpenAIClient client = OpenAIOkHttpClient.fromEnv(); // διαβάζει OPENAI_API_KEY :contentReference[oaicite:3]{index=3}

    public String chat(String userMessage, ChatRequest.MovieContext movie) {
        String movieContext = "";
        if (movie != null) {
            movieContext =
                    "Movie context:\n" +
                            "Title: " + safe(movie.title) + "\n" +
                            "Overview: " + safe(movie.overview) + "\n" +
                            "Release date: " + safe(movie.release_date) + "\n" +
                            "Rating: " + (movie.vote_average == null ? "" : movie.vote_average) + "\n\n";
        }

        String prompt =
                movieContext +
                        "You are a friendly movie assistant inside a movie website. " +
                        "Answer briefly and clearly. If asked for spoilers, warn first and ask if spoilers are ok.\n\n" +
                        "User: " + userMessage;

        ResponseCreateParams params = ResponseCreateParams.builder()
                .model("gpt-5.2")     // μπορείς να το αλλάξεις μετά
                .input(prompt)
                .build();

        Response response = client.responses().create(params);
        return response.output().toString();
    }

    private String safe(String s) { return s == null ? "" : s; }
}
