package com.movies2c.backend.controller;

import com.movies2c.backend.model.PostReply;
import com.movies2c.backend.model.User;
import com.movies2c.backend.model.dto.CreateReplyRequest;
import com.movies2c.backend.service.AuthenticationService;
import com.movies2c.backend.service.PostReplyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/replies")
public class PostReplyController {

    private final PostReplyService postReplyService;
    private final AuthenticationService authenticationService;

    public PostReplyController(PostReplyService postReplyService, AuthenticationService authenticationService) {
        this.postReplyService = postReplyService;
        this.authenticationService = authenticationService;
    }

    private User getUserFromAuthHeader(String authHeader) {
        String token = authHeader.replace("Bearer", "").trim();
        return authenticationService.getUserFromToken(token);
    }

    // 1) Create reply (thelei login)
    @PostMapping
    public PostReply createReply(@RequestHeader("Authorization") String authHeader,
                                 @PathVariable String postId,
                                 @RequestBody CreateReplyRequest request) {

        User user = getUserFromAuthHeader(authHeader);

        return postReplyService.createReply(
                postId,
                user.getId(),
                user.getUserName(),
                request.getText(),
                request.getParentReplyId()
        );
    }

    // 2) Get top-level replies
    @GetMapping
    public Page<PostReply> getTopLevelReplies(@PathVariable String postId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postReplyService.getTopLevelReplies(postId, pageable);
    }

    // 3) Get replies of a specific reply
    @GetMapping("/{parentReplyId}")
    public Page<PostReply> getChildReplies(@PathVariable String postId,
                                           @PathVariable String parentReplyId,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postReplyService.getChildReplies(postId, parentReplyId, pageable);
    }
}
