package com.example.backend.modules.history.repositories;


import com.example.backend.modules.history.models.History;
import com.example.backend.modules.history.viewmodels.HistoryRank;
import com.example.backend.modules.quiz.models.Quiz;
import com.example.backend.modules.room.models.Room;
import com.example.backend.modules.user.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryRepository  extends JpaRepository<History,Integer> {

    Optional<History> findByUserAndQuiz(User user, Quiz quiz);
    Optional<History> findByUserAndRoom(User user, Room room);

    @Query("Select h from History as h left join h.user as u left join h.room as r where h.user = :user and r IS NOT NULL")
    Page<History> getHistoryRoom(User user,Pageable paging);

    @Query("Select h from History as h left join h.user as u left join h.quiz as q where h.user = :user and q IS NOT NULL")
    Page<History> getHistorySingle(User user,Pageable paging);

    @Query("Select new com.example.backend.modules.history.viewmodels.HistoryRank(h.user,h.totalCorrect,h.score) from History as h left join h.user left join h.quiz as q where q.id = :quizId")
    Page<HistoryRank> getHistoryRankSingle(int quizId, Pageable paging);

    @Query("Select new com.example.backend.modules.history.viewmodels.HistoryRank(h.user,h.totalCorrect,h.score) from History as h left join h.user left join h.room as r where r.id = :roomId")
    Page<HistoryRank> getHistoryRankRoom(int roomId, Pageable paging);

    Optional<History> findByIdAndUser(int id,User user);
}
