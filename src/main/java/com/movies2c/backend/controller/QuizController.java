package com.movies2c.backend.controller;

import com.movies2c.backend.model.Quiz;
import com.movies2c.backend.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quizzes")
@CrossOrigin(origins = "*")
public class QuizController {

    @Autowired
    private QuizService quizService;

    // Δημιουργία νέου quiz
    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz) {
        Quiz createdQuiz = quizService.createQuiz(quiz);
        return new ResponseEntity<>(createdQuiz, HttpStatus.CREATED);
    }

    // Όλα τα quizzes
    @GetMapping
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        List<Quiz> quizzes = quizService.getAllQuizzes();
        return ResponseEntity.ok(quizzes);
    }



    // Quiz ανά ID
    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable String id) {
        Quiz quiz = quizService.getQuizById(id);
        return ResponseEntity.ok(quiz);
    }

    // Quiz ανά category (διορθωμένο)
//    @GetMapping("/category")
//    public ResponseEntity<List<Quiz>> getQuizzesByCategory(@RequestParam String category) {
//        List<Quiz> quizzes = quizService.getQuizzesByCategory(category);
////        if (quizzes.isEmpty()) {
////            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(quizzes);
////        }
//        return ResponseEntity.ok(quizzes);
//    }
@GetMapping("/category")
public ResponseEntity<List<Quiz>> getQuizzesByCategory(@RequestParam String category) {
    List<Quiz> quizzes = quizService.getQuizzesByCategory(category);
    return ResponseEntity.ok(quizzes);
}


    // Διαγραφή quiz
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable String id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }

    // Ενημέρωση quiz
    @PutMapping("/{id}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable String id, @RequestBody Quiz quizDetails) {
        Quiz updatedQuiz = quizService.updateQuiz(id, quizDetails);
        return ResponseEntity.ok(updatedQuiz);
    }

    // Όλες οι διαθέσιμες κατηγορίες
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = List.of(
                "Hollywood Classics",
                "Oscar Winners",
                "Science Fiction",
                "Comedy",
                "Drama & Romance",
                "Action & Adventure"
        );
        return ResponseEntity.ok(categories);
    }

    // Test endpoint
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> testEndpoint() {
        return ResponseEntity.ok(Map.of(
                "message", "Quiz API is working!",
                "status", "OK",
                "version", "1.0"
        ));
    }
}
