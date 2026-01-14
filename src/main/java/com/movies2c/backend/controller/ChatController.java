package com.movies2c.backend.controller;
import com.movies2c.backend.model.dto.ChatRequest;
import com.movies2c.backend.model.dto.ChatResponse;
import com.movies2c.backend.service.OpenAIChatService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final OpenAIChatService chatService;

    public ChatController(OpenAIChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest req) {
        if (req == null || req.message == null || req.message.trim().isEmpty()) {
            return new ChatResponse("Στείλε μου ένα μήνυμα 🙂");
        }
        String reply = chatService.chat(req.message.trim(), req.movie);

        return new ChatResponse(reply == null ? "" : reply);
    }
}
