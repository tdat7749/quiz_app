package com.example.backend.modules.quiz.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(length = 150,nullable = false)
    private String title;

    @Column(columnDefinition = "MEDIUMTEXT",nullable = true)
    private String thumbnail;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false,name = "order_number")
    private int order;

    @Column(nullable = false,name = "time_limit_seconds")
    private int timeLimit;

    @Column(nullable = false,name = "created_at")
    private Date createdAt;

    @Column(nullable = false,name = "updated_at")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "quiz_id",nullable = false)
    @JsonBackReference
    private Quiz quiz;

    @ManyToOne
    @JoinColumn(name = "question_type_id",nullable = false)
    @JsonBackReference
    private QuestionType questionType;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "question")
    @JsonManagedReference
    private List<Answer> answers;
}
