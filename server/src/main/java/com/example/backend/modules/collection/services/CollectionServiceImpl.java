package com.example.backend.modules.collection.services;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.collection.constant.CollectionConstants;
import com.example.backend.modules.collection.exceptions.CollectedException;
import com.example.backend.modules.collection.exceptions.NotYetCollectedException;
import com.example.backend.modules.collection.models.Collection;
import com.example.backend.modules.collection.repositories.CollectionRepository;
import com.example.backend.modules.quiz.constant.QuizConstants;
import com.example.backend.modules.quiz.exceptions.QuizNotFoundException;
import com.example.backend.modules.quiz.models.Quiz;
import com.example.backend.modules.quiz.services.QuizService;
import com.example.backend.modules.user.models.User;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CollectionServiceImpl implements CollectionService{

    private final QuizService quizService;
    private final CollectionRepository collectionRepository;

    public CollectionServiceImpl(
            QuizService quizService,
            CollectionRepository collectionRepository
    ){
        this.quizService = quizService;
        this.collectionRepository = collectionRepository;
    }

    @Override
    public boolean isCollected(User user, Quiz quiz) {
        return collectionRepository.existsByUserAndQuiz(user,quiz);
    }

    @Override
    public ResponseSuccess<Boolean> addToCollection(User user, int quizId) {
        var quiz = quizService.findById(quizId);
        if(quiz.isEmpty()){
            throw new QuizNotFoundException(QuizConstants.QUIZ_NOT_FOUND);
        }
        var isCollected = this.isCollected(user,quiz.get());
        if(isCollected){
            throw new CollectedException(CollectionConstants.COLLECTED);
        }

        Collection newCollection = Collection.builder()
                .user(user)
                .quiz(quiz.get())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        collectionRepository.save(newCollection);

        return new ResponseSuccess<>(CollectionConstants.ADD_TO_COLLECTION,true);
    }

    @Override
    public ResponseSuccess<Boolean> removeFromCollection(User user, int quizId) {
        var quiz = quizService.findById(quizId);
        if(quiz.isEmpty()){
            throw new QuizNotFoundException(QuizConstants.QUIZ_NOT_FOUND);
        }
        var collection = collectionRepository.findByUserAndQuiz(user,quiz.get());
        if(collection.isEmpty()){
            throw new NotYetCollectedException(CollectionConstants.NOT_YET_COLLECTED);
        }

        collectionRepository.delete(collection.get());

        return new ResponseSuccess<>(CollectionConstants.REMOVE_FROM_COLLECTION,true);

    }
}
