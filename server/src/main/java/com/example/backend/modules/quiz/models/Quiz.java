package com.example.backend.modules.quiz.models;

import com.example.backend.modules.collection.models.Collection;
import com.example.backend.modules.room.models.Room;
import com.example.backend.modules.topic.models.Topic;
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
@Table(name = "quizzes",indexes = {
        @Index(name="idx_slug",columnList = "slug")
})
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 300,nullable = false)
    private String summary;

    @Column(columnDefinition = "MEDIUMTEXT",nullable = false)
    private String description;

    @Column(columnDefinition = "MEDIUMTEXT",nullable = true)
    private String thumbnail;

    @Column(length = 150,nullable = false)
    private String title;

    @Column(name = "is_public",nullable = false)
    private Boolean isPublic;

    @Column(length = 170,nullable = false,unique = true)
    private String slug;

    @Column(name = "created_at",nullable = false)
    private Date createdAt;

    @Column(name = "updated_at",nullable = false)
    private Date updatedAt;

    //config orm
    @ManyToOne
    @JoinColumn(nullable = false,name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false,name = "topic_id")
    @JsonBackReference
    private Topic topic;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "quiz")
    @JsonManagedReference
    private List<Room> rooms;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "quiz")
    @JsonManagedReference
    private List<Collection> collections;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "quiz")
    @JsonManagedReference
    private List<Question> questions;
}
