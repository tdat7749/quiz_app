package com.example.backend.modules.quiz.services;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.filestorage.services.FileStorageService;
import com.example.backend.modules.quiz.constant.QuizConstants;
import com.example.backend.modules.quiz.dtos.CreateAnswerDTO;
import com.example.backend.modules.quiz.dtos.CreateQuestionDTO;
import com.example.backend.modules.quiz.dtos.EditQuestionDTO;
import com.example.backend.modules.quiz.exceptions.*;
import com.example.backend.modules.quiz.models.Answer;
import com.example.backend.modules.quiz.models.Question;
import com.example.backend.modules.quiz.models.Quiz;
import com.example.backend.modules.quiz.repositories.QuestionRepository;
import com.example.backend.modules.quiz.viewmodels.QuestionDetailVm;
import com.example.backend.modules.user.models.User;
import com.example.backend.utils.Utilities;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService{

    private final QuestionRepository questionRepository;
    private final FileStorageService fileStorageService;
    private final QuestionTypeService questionTypeService;

    private final QuizService quizService;

    public QuestionServiceImpl(
            QuestionRepository questionRepository,
            FileStorageService fileStorageService,
            QuestionTypeService questionTypeService,
            @Lazy QuizService quizService
    ){
        this.questionRepository = questionRepository;
        this.fileStorageService = fileStorageService;
        this.questionTypeService = questionTypeService;
        this.quizService = quizService;
    }

    @Override
    public Optional<Question> findById(int id) {
        return questionRepository.findById(id);
    }

    @Override
    @Transactional
    public Boolean createBulkQuestion(List<CreateQuestionDTO> listDto, Quiz quiz) throws IOException {
        List<Question> questionList = new ArrayList<>();

        for(CreateQuestionDTO item : listDto){
            var questionType = questionTypeService.findById(item.getQuestionTypeId());
            if(questionType.isEmpty()){
                throw new QuestionTypeNotFoundException(QuizConstants.QUESTION_TYPE_NOT_FOUND);
            }

            List<Answer> answerList = new ArrayList<>();

            for(CreateAnswerDTO i : item.getAnswers()){
                var newAnswer = Answer.builder()
                        .createdAt(new Date())
                        .title(i.getTitle())
                        .updatedAt(new Date())
                        .isCorrect(i.isCorrect())
                        .build();
                answerList.add(newAnswer);
            }

            var thumbnailUrl = fileStorageService.uploadFile(item.getThumbnail());

            var newQuestion = Question.builder()
                    .createdAt(new Date())
                    .order(item.getOrder())
                    .score(item.getScore())
                    .quiz(quiz)
                    .thumbnail(thumbnailUrl)
                    .title(item.getTitle())
                    .timeLimit(item.getTimeLimit())
                    .questionType(questionType.get())
                    .updatedAt(new Date())
                    .answers(answerList)
                    .build();

            questionList.add(newQuestion);
        }

        questionRepository.saveAll(questionList);

        return true;
    }

    @Override
    public ResponseSuccess<Boolean> editQuestion(User user,EditQuestionDTO dto) throws IOException {
        var isQuizOwner = quizService.existsByUserAndId(user,dto.getQuizId());
        if(!isQuizOwner){
            throw new NotOwnerQuizException(QuizConstants.NOT_OWNER_QUIZ);
        }

        var question = questionRepository.findById(dto.getQuestionId());
        if(question.isEmpty()){
            throw new QuestionNotFoundException(QuizConstants.QUESTION_NOT_FOUND);
        }

        question.get()
                .setTitle(dto.getTitle());
        question.get()
                .setScore(dto.getScore());
        question.get()
                .setTimeLimit(dto.getTimeLimit());

        if(dto.getThumbnail() != null){
            String thumbnailUrl = fileStorageService.uploadFile(dto.getThumbnail());
            question.get()
                    .setThumbnail(thumbnailUrl);
        }

        questionRepository.save(question.get());

        return new ResponseSuccess<>(QuizConstants.EDIT_QUESTION,true);
    }


    @Override
    public ResponseSuccess<Boolean> deleteQuestion(User user, int questionId,int quizId) {
        var isQuizOwner = quizService.existsByUserAndId(user,quizId);
        if(!isQuizOwner){
            throw new NotOwnerQuizException(QuizConstants.NOT_OWNER_QUIZ);
        }

        var question = questionRepository.findById(questionId);
        if(question.isEmpty()){
            throw new QuestionNotFoundException(QuizConstants.QUESTION_NOT_FOUND);
        }

        questionRepository.delete(question.get());

        return new ResponseSuccess<>(QuizConstants.DELETE_QUESTION,true);
    }

    @Override
    public ResponseSuccess<List<QuestionDetailVm>> getListQuestions(User user,int quizId) {
        var quiz = quizService.findById(quizId);
        if(quiz.isEmpty()){
            throw new QuizNotFoundException(QuizConstants.QUIZ_NOT_FOUND);
        }

        if(!quiz.get().getIsPublic()){
            if(!quiz.get().getUser().equals(user)){
                throw new QuizNotPublicException(QuizConstants.GET_QUESTION_QUIZ_NOT_PUBLIC);
            }
        }

        var listQuestion = quiz.get().getQuestions()
                .stream()
                .map(Utilities::getQuestionDetailVm)
                .sorted((q1,q2) -> (q1.getOrder()).compareTo(q1.getOrder()))
                .toList();

        return new ResponseSuccess<>("Thành công",listQuestion);
    }
}
