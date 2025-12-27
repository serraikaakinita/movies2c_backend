package com.movies2c.backend.controller;

import com.movies2c.backend.model.User;
import com.movies2c.backend.model.dto.UpdateUserRequest;
import com.movies2c.backend.model.dto.UserDto;
import com.movies2c.backend.service.AuthenticationService;
import com.movies2c.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.movies2c.backend.model.dto.ChangePasswordRequest;
import com.movies2c.backend.model.UserFavoriteMovies;
import com.movies2c.backend.model.dto.FavoriteMovieRequest;
import com.movies2c.backend.repositories.UserFavoriteMoviesRepository;

import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserFavoriteMoviesRepository favoriteMoviesRepository;

    @Autowired
    private UserService userService;

    private User getUserFromAuthHeader(String authHeader) {
        if (authHeader == null || authHeader.isBlank()) {
            throw new RuntimeException("Missing auth header");
        }
        String token = authHeader.replace("Bearer","").trim();
        return authenticationService.getUserFromToken(token);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(
            @RequestHeader (value = "Authorization", required = false) String authHeader
    ) {
        try {
            User user = getUserFromAuthHeader(authHeader);
            return ResponseEntity.ok(new UserDto(user.getId(), user.getUserName(), user.getEmail(), user.getBio(), user.getDate()));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateMe(
        @RequestHeader("Authorization") String authHeader,
        @RequestBody UpdateUserRequest request
    ) {
        try {
            User current = getUserFromAuthHeader(authHeader);
            User updated = userService.updateUser(current.getId(), request);
            return ResponseEntity.ok(new UserDto(updated.getId(), updated.getUserName(), updated.getEmail(), updated.getBio(), updated.getDate()));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/me/favorites/movies")
    public ResponseEntity<?> getFavoriteMovies(
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        try {
            User current = getUserFromAuthHeader(authHeader);
            List<UserFavoriteMovies> favorites = favoriteMoviesRepository.findByUserId(current.getId());
            return ResponseEntity.ok(favorites);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
    }

    @GetMapping ("/me/favorites/movies/count")
    public ResponseEntity<?> getUserFavoriteMovies(
            @RequestHeader(value = "Authorization", required = false) String authHeader ){
        try {
            User current = getUserFromAuthHeader(authHeader);
            long count = favoriteMoviesRepository.countByUserId(current.getId());
            return ResponseEntity.ok(count);
        }  catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
    }

    @PostMapping("/me/favorites/movies")
    public ResponseEntity<?> addFavoriteMovies(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody FavoriteMovieRequest request
    ) {
        try {
            User current = getUserFromAuthHeader(authHeader);

            if (request.getMovieId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing movie id");
            }

            var existing = favoriteMoviesRepository
                    .findByUserIdAndMovieId(current.getId(), request.getMovieId());
            if (existing.isPresent()) {
                return ResponseEntity.ok(existing.get());
            }

            UserFavoriteMovies fav = new UserFavoriteMovies();
            fav.setUserId(current.getId());
            fav.setMovieId(request.getMovieId());
            fav.setTitle(request.getTitle());
            fav.setPosterPath(request.getPosterPath());
            fav.setOverview(request.getOverview());
            fav.setReleaseDate(request.getReleaseDate());
            fav.setVoteAverage(request.getVoteAverage());
            fav.setCreatedAt(System.currentTimeMillis());

            favoriteMoviesRepository.save(fav);
            return ResponseEntity.status(HttpStatus.CREATED).body(fav);

            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
            }
        }
        @DeleteMapping("/me/favorites/movies/{movieId}")
        public ResponseEntity<?> deleteFavoriteMovies(
                @RequestHeader(value = "Authorization", required = false) String authHeader,
                @PathVariable("movieId") Long movieId
        ){
        try {
            User current = getUserFromAuthHeader(authHeader);
            favoriteMoviesRepository.deleteByUserIdAndMovieId(current.getId(), movieId);
            return ResponseEntity.ok("Removed from favorites");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
        }
    @PutMapping("/me/change-password")
    public ResponseEntity<String> changePassword(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody ChangePasswordRequest req
    ){
        try {
            User current = getUserFromAuthHeader(authHeader);

            userService.changePassword(current.getId(), req);

            return ResponseEntity.ok("Password changed");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public  ResponseEntity<UserDto> getUser(@PathVariable String id)
    {
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        User u = user.get();
        return ResponseEntity.ok(new UserDto(u.getId(), u.getUserName(), u.getEmail(), u.getBio(), u.getDate()));
    }
}
