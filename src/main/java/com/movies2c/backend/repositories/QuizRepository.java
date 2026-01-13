package com.movies2c.backend.repositories;

import com.movies2c.backend.model.Quiz;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface QuizRepository extends MongoRepository<Quiz, String> {

    // Χρησιμοποιούμε regex για να ταιριάζει ακριβώς αλλά αγνοεί κεφαλαία και spaces
    @Query("{ 'category': { $regex: ?0, $options: 'i' } }")
    List<Quiz> findByCategoryRegex(String category);
}
