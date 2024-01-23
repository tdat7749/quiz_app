package com.example.backend.modules.history.models;


import com.example.backend.modules.quiz.models.Quiz;
import com.example.backend.modules.room.models.Room;
import com.example.backend.modules.user.models.User;
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
@Table(name = "histories")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "total_correct",nullable = false)
    private int totalCorrect;

    @Column(name = "started_at",nullable = false)
    private Date startedAt;

    @Column(name = "finished_at",nullable = false)
    private Date finishedAt;

    @Column(nullable = false)
    private int score;

    @Column(name = "created_at",nullable = false)
    private Date createdAt;

    @Column(name = "updated_at",nullable = false)
    private Date updatedAt;


    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "room_id",nullable = true)
    private Room room;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "quiz_id",nullable = true)
    private Quiz quiz;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "history")
    @JsonManagedReference
    private List<HistoryAnswer> historyAnswers;

}

