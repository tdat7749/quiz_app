package com.example.backend.modules.collection.repositories;

import com.example.backend.modules.collection.models.Collection;
import com.example.backend.modules.quiz.models.Quiz;
import com.example.backend.modules.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CollectionRepository extends JpaRepository<Collection,Integer> {
    boolean existsByUserAndQuiz(User user, Quiz quiz);

    Optional<Collection> findByUserAndQuiz(User user, Quiz quiz);
}
