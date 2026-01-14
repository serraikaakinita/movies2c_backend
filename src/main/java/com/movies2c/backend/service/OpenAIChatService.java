package com.movies2c.backend.service;

import com.movies2c.backend.model.dto.ChatRequest;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OpenAIChatService {

    private final OpenAIClient client = OpenAIOkHttpClient.fromEnv();
    private final ObjectMapper mapper = new ObjectMapper();

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
                .model("gpt-5.2")
                .input(prompt)
                .build();

        Response response = client.responses().create(params);
        return extractText(response);
    }

    private String extractText(Response response) {
        if (response == null) return "";

        try {
            // Μετατρέπουμε το response σε JSON string
            String json = mapper.writeValueAsString(response);
            JsonNode root = mapper.readTree(json);

            StringBuilder sb = new StringBuilder();

            // Προσπαθούμε να βρούμε output[*].content[*].text (κλασική μορφή)
            JsonNode output = root.path("output");
            if (output.isArray()) {
                for (JsonNode item : output) {
                    JsonNode content = item.path("content");
                    if (content.isArray()) {
                        for (JsonNode c : content) {
                            String text = c.path("text").asText("");
                            if (!text.isBlank()) sb.append(text);
                        }
                    }
                }
            }

            String result = sb.toString().trim();
            if (!result.isBlank()) return result;

            // Fallback: αν για κάποιο λόγο είναι αλλού, γύρνα όλο το response σαν string
            return root.toString();

        } catch (Exception e) {
            // τελευταίο fallback
            return response.toString();
        }
    }


    private String safe(String s) {
        return s == null ? "" : s;
    }
}
