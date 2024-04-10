package com.example.backend.modules.user.models;

import com.example.backend.modules.collection.models.Collection;
import com.example.backend.modules.history.models.History;
import com.example.backend.modules.quiz.models.Quiz;
import com.example.backend.modules.room.models.Room;
import com.example.backend.modules.user.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users", indexes = {
        @Index(name="idx_username",columnList = "user_name")
})
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String password;

    @Column(name = "user_name",length = 40,nullable = false)
    private String userName;

    @Column(columnDefinition = "MEDIUMTEXT",nullable = true)
    private String token;

    @Column(columnDefinition = "MEDIUMTEXT",nullable = true)
    private String avatar;

    @Column(name = "display_name",nullable = true,length = 50)
    private String displayName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false,name = "created_at")
    private Date createdAt;

    @Column(nullable = false,name = "updated_at")
    private Date updatedAt;

    @ColumnDefault("true")
    @Column(name = "is_not_locked",nullable = false)
    private boolean isNotLocked;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @ColumnDefault("false")
    @Column(name = "is_enable",nullable = false)
    private Boolean isEnable;

    @Column(name = "google_id",nullable = true)
    private String googleId;

    @Override
    public java.util.Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));

        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnable;
    }


    // config orm
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "user")
    @JsonManagedReference
    private List<Quiz> quizzes;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "user")
    @JsonManagedReference
    private List<Room> rooms;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "user")
    @JsonManagedReference
    private List<Collection> collections;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "user")
    @JsonManagedReference
    private List<History> histories;

    @ManyToMany(mappedBy = "users")
    @JsonBackReference
    private List<Room> userRooms;

    public void addRoom(Room room){
        userRooms.add(room);
        room.getUsers().add(this);
    }

    public void removeRoom(Room room){
        userRooms.remove(room);
        room.getUsers().remove(this);
    }

}
