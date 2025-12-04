package com.movies2c.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Quiz")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Quiz {
    @Id
    private String id;
    private String title;
    private String  description;
    private String questions;
    private String answers;
}
