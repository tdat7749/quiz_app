package com.example.backend.modules.quiz.services;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.quiz.constant.QuizConstants;
import com.example.backend.modules.quiz.dtos.EditAnswerDTO;
import com.example.backend.modules.quiz.exceptions.AnswerNotFoundException;
import com.example.backend.modules.quiz.exceptions.NotOwnerAnswerException;
import com.example.backend.modules.quiz.exceptions.NotOwnerQuizException;
import com.example.backend.modules.quiz.repositories.AnswerRepository;
import com.example.backend.modules.quiz.viewmodels.AnswerVm;
import com.example.backend.modules.user.models.User;
import com.example.backend.utils.Utilities;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AnswerServiceImpl implements AnswerService{

    private final AnswerRepository answerRepository;
    private final QuizService quizService;

    public AnswerServiceImpl(
            AnswerRepository answerRepository,
            QuizService quizService
    ){
        this.answerRepository = answerRepository;
        this.quizService = quizService;
    }

    @Override
    public ResponseSuccess<AnswerVm> editAnswer(User user, EditAnswerDTO dto) {
        var quiz = quizService.findByUserAndId(user,dto.getQuizId());
        if(quiz.isEmpty()){
            throw new NotOwnerQuizException(QuizConstants.NOT_OWNER_QUIZ);
        }

        var answer = answerRepository.findById(dto.getAnswerId());
        if(answer.isEmpty()){
            throw new AnswerNotFoundException(QuizConstants.ANSWER_NOT_FOUND);
        }

        var answerQuestion = answer.get().getQuestion();

        //kiểm tra rằng question chứa answer này có nằm trong quiz của chử sở hữu không ?
        var checkQuestion = quiz.get().getQuestions().contains(answerQuestion);

        if(!checkQuestion){
            throw new NotOwnerAnswerException(QuizConstants.NOT_OWNER_ANSWER);
        }

        answer.get()
                .setTitle(dto.getTitle());
        answer.get()
                .setIsCorrect(dto.isCorrect());
        answer.get()
                .setUpdatedAt(new Date());

        var save = answerRepository.save(answer.get());

        var result = Utilities.getAnswerVm(save);

        return new ResponseSuccess<>(QuizConstants.EDIT_ANSWER,result);

    }

    @Override
    public ResponseSuccess<Boolean> deleteAnswer(User user, int answerId, int quizId) {
        var quiz = quizService.findByUserAndId(user,quizId);
        if(quiz.isEmpty()){
            throw new NotOwnerQuizException(QuizConstants.NOT_OWNER_QUIZ);
        }

        var answer = answerRepository.findById(answerId);
        if(answer.isEmpty()){
            throw new AnswerNotFoundException(QuizConstants.ANSWER_NOT_FOUND);
        }

        var answerQuestion = answer.get().getQuestion();

        //kiểm tra rằng question chứa answer này có nằm trong quiz của chử sở hữu không ?
        var checkQuestion = quiz.get().getQuestions().contains(answerQuestion);

        if(!checkQuestion){
            throw new NotOwnerAnswerException(QuizConstants.NOT_OWNER_ANSWER);
        }

        answerRepository.delete(answer.get());

        return new ResponseSuccess<>(QuizConstants.DELETE_ANSWER,true);

    }
}
