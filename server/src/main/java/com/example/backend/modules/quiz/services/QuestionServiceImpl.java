package com.example.backend.modules.quiz.services;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.filestorage.services.FileStorageService;
import com.example.backend.modules.quiz.constant.QuizConstants;
import com.example.backend.modules.quiz.dtos.CreateAnswerDTO;
import com.example.backend.modules.quiz.dtos.CreateQuestionDTO;
import com.example.backend.modules.quiz.dtos.EditQuestionDTO;
import com.example.backend.modules.quiz.dtos.EditQuestionThumbnailDTO;
import com.example.backend.modules.quiz.exceptions.*;
import com.example.backend.modules.quiz.models.Answer;
import com.example.backend.modules.quiz.models.Question;
import com.example.backend.modules.quiz.models.Quiz;
import com.example.backend.modules.quiz.repositories.AnswerRepository;
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

    private final AnswerRepository answerRepository;


    public QuestionServiceImpl(
            QuestionRepository questionRepository,
            FileStorageService fileStorageService,
            QuestionTypeService questionTypeService,
            @Lazy QuizService quizService,
            AnswerRepository answerRepository
    ){
        this.questionRepository = questionRepository;
        this.fileStorageService = fileStorageService;
        this.questionTypeService = questionTypeService;
        this.quizService = quizService;
        this.answerRepository = answerRepository;
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

            String thumbnailUrl = null;

            if(item.getThumbnail() != null){
                thumbnailUrl = fileStorageService.uploadFile(item.getThumbnail());
            }

            var questionBuilder = Question.builder()
                    .createdAt(new Date())
                    .order(item.getOrder())
                    .score(item.getScore())
                    .quiz(quiz)
                    .thumbnail(thumbnailUrl)
                    .title(item.getTitle())
                    .timeLimit(item.getTimeLimit())
                    .questionType(questionType.get())
                    .updatedAt(new Date())
                    .build();

            var newQuestion = questionRepository.save(questionBuilder);

            List<Answer> answerList = new ArrayList<>();

            for(CreateAnswerDTO i : item.getAnswers()){
                var isCorrect = i.getIsCorrect().equals("true");
                var newAnswer = Answer.builder()
                        .createdAt(new Date())
                        .title(i.getTitle())
                        .updatedAt(new Date())
                        .isCorrect(isCorrect)
                        .question(newQuestion)
                        .build();
                answerList.add(newAnswer);
            }

            answerRepository.saveAll(answerList);
        }
        return true;
    }

    @Override
    public ResponseSuccess<QuestionDetailVm> editQuestion(User user,EditQuestionDTO dto){
        var isQuizOwner = quizService.existsByUserAndId(user,dto.getQuizId());
        if(!isQuizOwner){
            throw new NotOwnerQuizException(QuizConstants.NOT_OWNER_QUIZ);
        }

        var question = questionRepository.findById(dto.getQuestionId());
        if(question.isEmpty()){
            throw new QuestionNotFoundException(QuizConstants.QUESTION_NOT_FOUND);
        }

        var questionType = questionTypeService.findById(dto.getQuestionTypeId());
        if(questionType.isEmpty()){
            throw new QuestionTypeNotFoundException(QuizConstants.QUESTION_TYPE_NOT_FOUND);
        }

        question.get()
                .setTitle(dto.getTitle());
        question.get()
                .setScore(dto.getScore());
        question.get()
                .setTimeLimit(dto.getTimeLimit());
        question.get()
                .setQuestionType(questionType.get());

        var save = questionRepository.save(question.get());

        return new ResponseSuccess<>(QuizConstants.EDIT_QUESTION,Utilities.getQuestionDetailVm(save));
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

//        var isOwner = this.quizService.existsByUserAndId(user,quizId);
//        if(!isOwner){
//            if(!quiz.get().getIsPublic()){
//                throw new QuizNotPublicException(QuizConstants.QUIZ_NOT_PUBLIC);
//            }
//        }

        var listQuestion = quiz.get().getQuestions()
                .stream()
                .map(Utilities::getQuestionDetailVm)
                .sorted((q1,q2) -> (q1.getOrder()).compareTo(q1.getOrder()))
                .toList();

        return new ResponseSuccess<>("Thành công",listQuestion);
    }

    @Override
    public ResponseSuccess<QuestionDetailVm> createQuestion(User user,CreateQuestionDTO dto,int quizId) throws IOException {
        var questionType = questionTypeService.findById(dto.getQuestionTypeId());
        if(questionType.isEmpty()){
            throw new QuestionTypeNotFoundException(QuizConstants.QUESTION_TYPE_NOT_FOUND);
        }

        var quiz = quizService.findById(quizId);
        if(quiz.isEmpty()){
            throw new QuizNotFoundException(QuizConstants.QUIZ_NOT_FOUND);
        }

        var isOnwer = this.quizService.existsByUserAndId(user,quiz.get().getId());
        if(!isOnwer){
            throw new NotOwnerQuizException(QuizConstants.NOT_OWNER_QUIZ);
        }

        String thumbnailUrl = null;

        if(dto.getThumbnail() != null){
            thumbnailUrl = fileStorageService.uploadFile(dto.getThumbnail());
        }
        var questionBuilder = Question.builder()
                .createdAt(new Date())
                .order(dto.getOrder())
                .score(dto.getScore())
                .quiz(quiz.get())
                .thumbnail(thumbnailUrl)
                .title(dto.getTitle())
                .timeLimit(dto.getTimeLimit())
                .questionType(questionType.get())
                .updatedAt(new Date())
                .build();

        var newQuestion = questionRepository.save(questionBuilder);

        List<Answer> answerList = new ArrayList<>();
        var answers = dto.getAnswers();
        for(CreateAnswerDTO i : answers){
            var isCorrect = i.getIsCorrect().equals("true");
            var newAnswer = Answer.builder()
                    .createdAt(new Date())
                    .title(i.getTitle())
                    .updatedAt(new Date())
                    .isCorrect(isCorrect)
                    .question(newQuestion)
                    .build();
            answerList.add(newAnswer);
        }

        var saveAnswers = answerRepository.saveAll(answerList);

        newQuestion.setAnswers(saveAnswers);

        return new ResponseSuccess<>("Thêm câu hỏi mới thành công",Utilities.getQuestionDetailVm(newQuestion));
    }

    @Override
    @Transactional
    public ResponseSuccess<String> editQuestionThumbnail(User user, EditQuestionThumbnailDTO dto) throws IOException {
        var isQuizOwner = quizService.existsByUserAndId(user,dto.getQuizId());
        if(!isQuizOwner){
            throw new NotOwnerQuizException(QuizConstants.NOT_OWNER_QUIZ);
        }

        var question = questionRepository.findById(dto.getQuestionId());
        if(question.isEmpty()){
            throw new QuestionNotFoundException(QuizConstants.QUESTION_NOT_FOUND);
        }

        var thumbnailUrl = this.fileStorageService.uploadFile(dto.getThumbnail());

        question.get()
                .setThumbnail(thumbnailUrl);

        questionRepository.save(question.get());

        return new ResponseSuccess<>("Thay đổi hình ảnh câu hỏi thành công",thumbnailUrl);
    }
}
