package com.example.backend.modules.history.repositories;


import com.example.backend.modules.history.models.History;
import com.example.backend.modules.user.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HistoryRepository  extends JpaRepository<History,Integer> {


    @Query("Select h from History as h left join h.user as u left join h.room as r where h.user = :user")
    Page<History> getHistoryRoom(User user,Pageable paging);

    @Query("Select h from History as h left join h.user as u left join h.quiz as q where h.user = :user")
    Page<History> getHistorySingle(User user,Pageable paging);

    Optional<History> findByIdAndUser(int id,User user);
}
