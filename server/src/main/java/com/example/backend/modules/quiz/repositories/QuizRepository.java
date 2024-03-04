package com.example.backend.modules.quiz.repositories;


import com.example.backend.modules.quiz.models.Quiz;
import com.example.backend.modules.topic.models.Topic;
import com.example.backend.modules.user.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz,Integer> {
    Optional<Quiz> findById(int id);

    Optional<Quiz> findBySlug(String slug);

    Optional<Quiz> findByUserAndId(User user,int id);

    boolean existsByUserAndId(User user,int id);

    @Query("select q from Quiz as q where q.isPublic = true and q.topic = :topic and q.title LIKE %:keyword%")
    Page<Quiz> getListPublicQuizzes(Topic topic,String keyword, Pageable paging);

    @Query("select q from Quiz as q where q.user = :user and q.title LIKE %:keyword%")
    Page<Quiz> getMyListQuizzes(User user,String keyword, Pageable paging);

    @Query("select q from Quiz as q left join q.collections as c where c.user = :user and q.title LIKE %:keyword% and q.isPublic = true")
    Page<Quiz> getMyListCollection(User user,String keyword, Pageable paging);

    @Query("select q, count(c) as count from Quiz as q left join q.collections as c where q.isPublic = true group by q order by count desc")
    List<Quiz> getTop10QuizCollection(Pageable pageable);

    @Query("select q from Quiz as q where q.isPublic = true order by q.createdAt desc ")
    List<Quiz> get10QuizLatest(Pageable pageable);
}
