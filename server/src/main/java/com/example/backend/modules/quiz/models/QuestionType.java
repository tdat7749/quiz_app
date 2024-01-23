package com.example.backend.modules.quiz.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "questions_type")
public class QuestionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 150,nullable = false)
    private String title;

    @Column(nullable = false,name = "created_at")
    private String createdAt;

    @Column(nullable = false,name = "updated_at")
    private String updatedAt;

    // orm

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "questionType")
    @JsonManagedReference
    private List<Question> questions;
}
