package com.movies2c.backend.controller;

import com.movies2c.backend.model.Post;
import com.movies2c.backend.model.User;
import com.movies2c.backend.model.dto.CreatePostRequest;
import com.movies2c.backend.service.AuthenticationService;
import com.movies2c.backend.service.MediaService;
import com.movies2c.backend.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.movies2c.backend.model.dto.RatePostRequest;




@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final AuthenticationService authenticationService;
    private final MediaService mediaService;

    public PostController(PostService postService,
                          AuthenticationService authenticationService,
                          MediaService mediaService) {
        this.postService = postService;
        this.authenticationService = authenticationService;
        this.mediaService = mediaService;
    }

    private User getUserFromAuthHeader(String authHeader) {
        String token = authHeader.replace("Bearer", "").trim();
        return authenticationService.getUserFromToken(token);
    }

    @PostMapping
    public Post createPost(@RequestHeader("Authorization") String authHeader,
                           @RequestBody CreatePostRequest request) {

        User user = getUserFromAuthHeader(authHeader);

        Post post = new Post();
        post.setMovieId(request.getMovieId());
        post.setMediaType(request.getMediaType());
        post.setMediaId(request.getMediaId());
        post.setCaption(request.getCaption());

        post.setUserId(user.getId());
        post.setUserName(user.getUserName());

        post.setCreatedAt(System.currentTimeMillis());
        post.setAvgRating(0);
        post.setRatingCount(0);

        return postService.createPost(post);
    }

    @PostMapping("/upload")
    public ResponseEntity<Post> createPostWithUpload(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("file") MultipartFile file,
            @RequestParam("movieId") String movieId,
            @RequestParam(value = "caption", required = false) String caption,
            @RequestParam("mediaType") String mediaType
    ) throws Exception {

        User user = getUserFromAuthHeader(authHeader);

        String mediaId = mediaService.saveFile(file);

        Post post = new Post();
        post.setMovieId(movieId);
        post.setUserId(user.getId());
        post.setUserName(user.getUserName());
        post.setMediaType(mediaType);
        post.setMediaId(mediaId);
        post.setCaption(caption);

        post.setCreatedAt(System.currentTimeMillis());
        post.setAvgRating(0);
        post.setRatingCount(0);

        return ResponseEntity.ok(postService.createPost(post));
    }
    @GetMapping
    public Page<Post> getFeed(
            @RequestParam(defaultValue = "top") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        if (sort.equalsIgnoreCase("latest")) {
            return postService.getLatest(pageable);
        }

        return postService.getTopRated(pageable);
    }
    @PostMapping("/{postId}/rate")
    public Post ratePost(@RequestHeader("Authorization") String authHeader,
                         @PathVariable String postId,
                         @RequestBody RatePostRequest request) {

        User user = getUserFromAuthHeader(authHeader);

        return postService.ratePost(postId, user.getId(), request.getValue());
    }


}
