package com.example.backend.modules.room.models;

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
@Table(name = "game_modes",indexes = {
        @Index(name="idx_mode_code",columnList = "modeCode")
})
public class GameMode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "mode_name",nullable = false)
    private String modeName;

    @Column(name = "mode_code",nullable = false,unique = true)
    private String modeCode;

    @Column(name = "created_at",nullable = false)
    private Date createdAt;

    @Column(name = "updated_at",nullable = false)
    private Date updatedAt;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "gameMode")
    @JsonManagedReference
    private List<Room> rooms;
}
