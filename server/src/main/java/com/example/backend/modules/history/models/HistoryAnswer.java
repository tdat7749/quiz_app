package com.example.backend.modules.history.models;

import com.example.backend.modules.quiz.models.Answer;
import com.example.backend.modules.quiz.models.Question;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "history_answers")
public class HistoryAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "is_correct",nullable = false)
    private Boolean isCorrect;

    @Column(name = "created_at",nullable = false)
    private Date createdAt;

    @Column(name = "updated_at",nullable = false)
    private Date updatedAt;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "history_id",nullable = false)
    private History history;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "question_id",nullable = false)
    private Question question;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "answer_id")
    private Answer answer;
}
