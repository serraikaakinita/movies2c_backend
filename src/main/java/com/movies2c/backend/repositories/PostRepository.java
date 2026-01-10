package com.movies2c.backend.repositories;

import com.movies2c.backend.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;





public interface PostRepository extends MongoRepository<Post, String> {
    java.util.List<Post> findAllByOrderByCreatedAtDesc();
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Post> findAllByOrderByAvgRatingDescRatingCountDescCreatedAtDesc(Pageable pageable);

}
