package com.movies2c.backend.repositories;

import com.movies2c.backend.model.PostRating;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PostRatingRepository extends MongoRepository<PostRating, String> {

    Optional<PostRating> findByPostIdAndUserId(String postId, String userId);

    List<PostRating> findAllByPostId(String postId);

    long countByPostId(String postId);
}
