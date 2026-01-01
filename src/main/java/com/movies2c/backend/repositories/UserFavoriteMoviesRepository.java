package com.movies2c.backend.repositories;

import com.movies2c.backend.model.UserFavoriteMovies;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserFavoriteMoviesRepository extends MongoRepository<UserFavoriteMovies, String> {
    List<UserFavoriteMovies> findByUserId(String userId);
    Optional<UserFavoriteMovies> findByUserIdAndMovieId(String userId, Long movieId);
    long countByUserId(String userId);
    void deleteByUserIdAndMovieId(String userId, Long movieId);
}

