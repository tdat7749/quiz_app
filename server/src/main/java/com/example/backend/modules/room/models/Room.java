package com.example.backend.modules.room.models;

import com.example.backend.modules.history.models.History;
import com.example.backend.modules.quiz.models.Quiz;
import com.example.backend.modules.user.models.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "rooms",indexes = {
        @Index(name="idx_room_pin",columnList = "roomPin")
})
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "room_name",nullable = false,length = 100)
    private String roomName;

    @Column(name = "room_pin",nullable = false)
    private String roomPin;

    @Column(name = "created_at",nullable = false)
    private Date createdAt;

    @Column(name = "time_start",nullable = true)
    private LocalDateTime timeStart;

    @Column(name = "time_end",nullable = true)
    private LocalDateTime timeEnd;

    @Column(name = "is_closed",nullable = false)
    private boolean isClosed;

    @ManyToOne
    @JoinColumn(name = "host_id",nullable = false)
    @JsonBackReference
    private User user;


    @ManyToOne
    @JoinColumn(name = "quiz_id",nullable = false)
    @JsonBackReference
    private Quiz quiz;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "room")
    @JsonManagedReference
    private List<History> histories;

}
