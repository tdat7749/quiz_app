package com.example.backend.modules.room.repositories;

import com.example.backend.modules.room.models.Room;
import com.example.backend.modules.user.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room,Integer> {
    boolean existsByUserAndId(User user, int roomId);

    Optional<Room> findByRoomPin(String roomPin);


    @Query("select r from Room as r left join r.user as u left join r.quiz where r.user = :user and r.roomName LIKE %:keyword%")
    Page<Room> getMyListRooms(User user,String keyword, Pageable paging);
}
