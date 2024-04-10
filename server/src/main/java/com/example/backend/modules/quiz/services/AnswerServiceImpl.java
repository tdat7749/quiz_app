package com.example.backend.modules.quiz.services;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.quiz.constant.QuizConstants;
import com.example.backend.modules.quiz.dtos.EditAnswerDTO;
import com.example.backend.modules.quiz.exceptions.AnswerNotFoundException;
import com.example.backend.modules.quiz.exceptions.AtLeastOneAnswerIsCorrectException;
import com.example.backend.modules.quiz.exceptions.NotOwnerAnswerException;
import com.example.backend.modules.quiz.exceptions.NotOwnerQuizException;
import com.example.backend.modules.quiz.models.Answer;
import com.example.backend.modules.quiz.repositories.AnswerRepository;
import com.example.backend.modules.quiz.viewmodels.AnswerVm;
import com.example.backend.modules.user.models.User;
import com.example.backend.utils.Utilities;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    public Optional<Answer> findById(int id) {
        return answerRepository.findById(id);
    }

    @Override
    @Transactional
    public ResponseSuccess<List<AnswerVm>> editAnswer(User user, EditAnswerDTO dto) {
        var quiz = quizService.findByUserAndId(user,dto.getQuizId());
        if(quiz.isEmpty()){
            throw new NotOwnerQuizException(QuizConstants.NOT_OWNER_QUIZ);
        }

        //check coi xem có trường iscorrect nào true không ?
        boolean flag = false;
        for(var item : dto.getAnswers()){
            flag = item.isCorrect();
            if(flag){
                break;
            }
        }

        if(!flag){
            throw new AtLeastOneAnswerIsCorrectException(QuizConstants.AT_LEAST_ONE_ANSWER_IS_CORRECT);
        }

        List<AnswerVm> listAnswer = new ArrayList<>();

        for(var item : dto.getAnswers()){
            var answer = answerRepository.findById(item.getId());
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
                    .setTitle(item.getTitle());
            answer.get()
                    .setIsCorrect(item.isCorrect());
            answer.get()
                    .setUpdatedAt(new Date());

            var save = answerRepository.save(answer.get());
            listAnswer.add(Utilities.getAnswerVm(save));
        }

        return new ResponseSuccess<>(QuizConstants.EDIT_ANSWER,listAnswer);

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
