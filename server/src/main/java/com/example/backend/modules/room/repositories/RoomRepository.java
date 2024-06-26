package com.example.backend.modules.room.repositories;

import com.example.backend.modules.room.models.Room;
import com.example.backend.modules.user.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room,Integer> {
    boolean existsByUserAndId(User user, int roomId);

    @Query("select count (r) > 0 from Room as r left join r.users as u where r.id = :roomId and u.id = :userId")
    boolean existsByIdAndUser(int roomId,int userId);

    Optional<Room> findByRoomPin(String roomPin);

    @Query("select count(u) from Room as r left join r.users as u where r = :room")
    long countUserInRoom(Room room);

    @Query("select r from Room as r left join r.user as u left join r.quiz where r.user = :user and r.roomName LIKE %:keyword%")
    Page<Room> getMyListRooms(User user,String keyword, Pageable paging);

    @Query("select r from Room as r left join r.users as u where u = :user")
    Page<Room> getJoinedRoom(User user,Pageable paging);

    @Query("select r.users from Room as r where r = :room")
    Page<User> getUsersInRoom(Room room,Pageable paging);
}
