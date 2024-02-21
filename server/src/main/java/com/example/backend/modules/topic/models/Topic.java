package com.example.backend.modules.topic.models;

import com.example.backend.modules.quiz.models.Quiz;
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
@Table(name = "topics",indexes = {
        @Index(name="idx_slug",columnList = "slug")
})
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 150,nullable = false)
    private String title;

    @Column(columnDefinition = "MEDIUMTEXT",nullable = false)
    private String thumbnail;

    @Column(length = 170,nullable = false,unique = true)
    private String slug;

    @Column(name = "created_at",nullable = false)
    private Date createdAt;

    @Column(name = "updated_at",nullable = false)
    private Date updatedAt;

    //orm config
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "topic")
    @JsonManagedReference
    private List<Quiz> quizzes;
}
