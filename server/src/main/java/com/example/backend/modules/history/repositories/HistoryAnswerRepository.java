package com.example.backend.modules.history.repositories;


import com.example.backend.modules.history.models.History;
import com.example.backend.modules.history.models.HistoryAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryAnswerRepository extends JpaRepository<HistoryAnswer,Integer> {

    @Modifying
    @Query("delete from HistoryAnswer as ha where  ha.history.id = :historyId")
    void deleteByHistoryId(int historyId);

    @Query("Select ha from HistoryAnswer as ha left join ha.question where ha.history = :history")
    List<HistoryAnswer> getListHistoryAnswer(History history);
}
