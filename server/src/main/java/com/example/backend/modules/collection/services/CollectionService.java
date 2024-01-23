package com.example.backend.modules.collection.services;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.quiz.models.Quiz;
import com.example.backend.modules.user.models.User;
import org.springframework.stereotype.Service;


@Service
public interface CollectionService {
    boolean isCollected(User user, int quizId);

    ResponseSuccess<Boolean> addToCollection(User user,int quizId);

    ResponseSuccess<Boolean> removeFromCollection(User user, int quizId);
}
