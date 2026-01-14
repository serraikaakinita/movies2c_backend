package com.movies2c.backend.service;

import com.movies2c.backend.model.Quiz;
import com.movies2c.backend.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    public Quiz createQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Quiz getQuizById(String id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
    }

    public List<Quiz> getQuizzesByCategory(String category) {
        return quizRepository.findByCategory(category);
    }

    public void deleteQuiz(String id) {
        quizRepository.deleteById(id);
    }

    public Quiz updateQuiz(String id, Quiz quizDetails) {
        Quiz quiz = getQuizById(id);
        quiz.setTitle(quizDetails.getTitle());
        quiz.setDescription(quizDetails.getDescription());
        quiz.setCategory(quizDetails.getCategory());
        quiz.setQuestions(quizDetails.getQuestions());
        return quizRepository.save(quiz);
    }
}
