package com.example.backend.modules.quiz.models;

import com.example.backend.modules.history.models.HistoryAnswer;
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
@Table(name = "answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 150,nullable = false)
    private String title;

    @Column(name = "is_correct",nullable = false)
    private Boolean isCorrect;

    @Column(name = "created_at",nullable = false)
    private Date createdAt;

    @Column(name = "updated_at",nullable = false)
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "question_id",nullable = false)
    @JsonBackReference
    private Question question;


    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "answer")
    @JsonManagedReference
    private List<HistoryAnswer> historyAnswers;
}
