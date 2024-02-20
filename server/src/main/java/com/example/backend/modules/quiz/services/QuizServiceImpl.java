package com.example.backend.modules.quiz.services;


import com.example.backend.commons.AppConstants;
import com.example.backend.commons.ResponsePaging;
import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.filestorage.services.FileStorageService;
import com.example.backend.modules.quiz.constant.QuizConstants;
import com.example.backend.modules.quiz.dtos.CreateQuizDTO;
import com.example.backend.modules.quiz.dtos.EditQuizDTO;
import com.example.backend.modules.quiz.dtos.EditQuizThumbnail;
import com.example.backend.modules.quiz.exceptions.NotOwnerQuizException;
import com.example.backend.modules.quiz.exceptions.QuizNotFoundException;
import com.example.backend.modules.quiz.exceptions.QuizNotPublicException;
import com.example.backend.modules.quiz.exceptions.QuizSlugUsedException;
import com.example.backend.modules.quiz.models.Quiz;
import com.example.backend.modules.quiz.repositories.QuizRepository;
import com.example.backend.modules.quiz.viewmodels.QuizDetailVm;
import com.example.backend.modules.quiz.viewmodels.QuizVm;
import com.example.backend.modules.room.models.Room;
import com.example.backend.modules.room.viewmodels.RoomVm;
import com.example.backend.modules.topic.constant.TopicConstants;
import com.example.backend.modules.topic.exceptions.TopicNotFoundException;
import com.example.backend.modules.topic.services.TopicService;
import com.example.backend.modules.user.models.User;
import com.example.backend.utils.Utilities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class QuizServiceImpl implements QuizService{
    private final QuizRepository quizRepository;
    private final TopicService topicService;
    private final FileStorageService fileStorageService;

    private final QuestionService questionService;

    public QuizServiceImpl(
            QuizRepository quizRepository,
            TopicService topicService,
            FileStorageService fileStorageService,
            QuestionService questionService
    ){
        this.topicService = topicService;
        this.quizRepository = quizRepository;
        this.fileStorageService = fileStorageService;
        this.questionService = questionService;
    }

    @Override
    public Optional<Quiz> findById(int id) {
        return quizRepository.findById(id);
    }

    @Override
    public Optional<Quiz> findBySlug(String slug) {
        return quizRepository.findBySlug(slug);
    }

    @Override
    public Optional<Quiz> findByUserAndId(User user, int id) {
        return quizRepository.findByUserAndId(user,id);
    }

    @Override
    public boolean existsByUserAndId(User user, int id) {
        return quizRepository.existsByUserAndId(user,id);
    }

    @Override
    public ResponseSuccess<ResponsePaging<List<QuizVm>>> getMyListQuizzes(String keyword, String sortBy, int pageIndex, User user) {
        Pageable paging = PageRequest.of(pageIndex, AppConstants.PAGE_SIZE, Sort.by(Sort.Direction.DESC,sortBy));

        Page<Quiz> pagingResult = quizRepository.getMyListQuizzes(user,keyword,paging);

        List<QuizVm> roomVmList = pagingResult.stream().map(Utilities::getQuizVm).toList();

        ResponsePaging result = ResponsePaging.builder()
                .data(roomVmList)
                .totalPage(pagingResult.getTotalPages())
                .totalRecord((int) pagingResult.getTotalElements())
                .build();

        return new ResponseSuccess<>("Thành công",result);
    }

//    @Override
//    public ResponseSuccess<ResponsePaging<List<QuizVm>>> getMyListCollection(String keyword, String sortBy, int pageIndex, User user) {
//        Pageable paging = PageRequest.of(pageIndex, AppConstants.PAGE_SIZE, Sort.by(Sort.Direction.DESC,sortBy));
//
//        Page<Quiz> pagingResult = quizRepository.getListPublicQuizzes(topic.get(),keyword,paging);
//
//        List<QuizVm> roomVmList = pagingResult.stream().map(Utilities::getQuizVm).toList();
//
//        ResponsePaging result = ResponsePaging.builder()
//                .data(roomVmList)
//                .totalPage(pagingResult.getTotalPages())
//                .totalRecord((int) pagingResult.getTotalElements())
//                .build();
//    }

    @Override
    public ResponseSuccess<ResponsePaging<List<QuizVm>>> getListPublicQuizzes(String keyword, String sortBy, int pageIndex, int topicId) {

        var topic = topicService.findById(topicId);
        if(topic.isEmpty()){
            throw new TopicNotFoundException(TopicConstants.TOPIC_NOT_FOUND);
        }

        Pageable paging = PageRequest.of(pageIndex, AppConstants.PAGE_SIZE, Sort.by(Sort.Direction.DESC,sortBy));

        Page<Quiz> pagingResult = quizRepository.getListPublicQuizzes(topic.get(),keyword,paging);

        List<QuizVm> roomVmList = pagingResult.stream().map(Utilities::getQuizVm).toList();

        ResponsePaging result = ResponsePaging.builder()
                .data(roomVmList)
                .totalPage(pagingResult.getTotalPages())
                .totalRecord((int) pagingResult.getTotalElements())
                .build();

        return new ResponseSuccess<>("Thành công",result);
    }

    @Override
    public ResponseSuccess<QuizDetailVm> getQuizDetail(int quizId) {
        var quiz = quizRepository.findById(quizId);
        if(quiz.isEmpty()){
            throw new QuizNotFoundException(QuizConstants.QUIZ_NOT_FOUND);
        }

        if(!quiz.get().getIsPublic()){
            throw new QuizNotPublicException(QuizConstants.QUIZ_NOT_PUBLIC);
        }

        var result = Utilities.getQuizDetailVm(quiz.get());

        return new ResponseSuccess<>("Thành công",result);
    }

    @Override
    @Transactional
    public ResponseSuccess<QuizVm> createQuiz(User user, CreateQuizDTO dto) throws IOException {
        var foundQuizBySlug = quizRepository.findBySlug(dto.getSlug());
        if(foundQuizBySlug.isPresent()){
            throw new QuizSlugUsedException(QuizConstants.QUIZ_SLUG_USED);
        }

        var topic = topicService.findById(dto.getTopicId());
        if(topic.isEmpty()){
            throw new TopicNotFoundException(TopicConstants.TOPIC_NOT_FOUND);
        }

        var thumbnailUrl = fileStorageService.uploadFile(dto.getThumbnail());

        var newQuiz = Quiz.builder()
                .createdAt(new Date())
                .updatedAt(new Date())
                .description(dto.getDescription())
                .slug(dto.getSlug())
                .summary(dto.getSummary())
                .isPublic(dto.isPublic())
                .user(user)
                .title(dto.getTitle())
                .thumbnail(thumbnailUrl)
                .topic(topic.get())
                .build();

        var saveQuiz = quizRepository.save(newQuiz);

        questionService.createBulkQuestion(dto.getQuestions(),saveQuiz);

        var result = Utilities.getQuizVm(saveQuiz);

        return new ResponseSuccess<>(QuizConstants.CREATE_QUIZ,result);
    }

    @Override
    public ResponseSuccess<QuizVm> editQuiz(User user, EditQuizDTO dto) {
        var quiz = quizRepository.findByUserAndId(user,dto.getQuizId());
        if(quiz.isEmpty()){
            throw new NotOwnerQuizException(QuizConstants.NOT_OWNER_QUIZ);
        }

        quiz.get()
                .setSummary(dto.getSummary());
        quiz.get()
                .setDescription(dto.getDescription());
        quiz.get()
                .setTitle(dto.getTitle());
        quiz.get()
                .setSlug(dto.getSlug());

        if(dto.getTopicId() != null){
            var topic = topicService.findById(dto.getTopicId());
            if(topic.isEmpty()){
                throw new TopicNotFoundException(TopicConstants.TOPIC_NOT_FOUND);
            }
            quiz.get()
                    .setTopic(topic.get());
        }
        var save = quizRepository.save(quiz.get());
        var result = Utilities.getQuizVm(save);

        return new ResponseSuccess<>(QuizConstants.EDIT_QUIZ,result);
    }

    @Override
    public ResponseSuccess<Boolean> deleteQuiz(User user, int quizId) {
        var quiz = quizRepository.findByUserAndId(user,quizId);
        if(quiz.isEmpty()){
            throw new NotOwnerQuizException(QuizConstants.NOT_OWNER_QUIZ);
        }

        quizRepository.delete(quiz.get());

        return new ResponseSuccess<>(QuizConstants.DELETE_QUIZ,true);
    }

    @Override
    @Transactional
    public ResponseSuccess<String> editThumbnail(User user, EditQuizThumbnail dto) throws IOException {
        var quiz = quizRepository.findByUserAndId(user,dto.getQuizId());
        if(quiz.isEmpty()){
            throw new NotOwnerQuizException(QuizConstants.NOT_OWNER_QUIZ);
        }

        String thumbnailUrl = fileStorageService.uploadFile(dto.getThumbnail());

        quiz.get()
                .setThumbnail(thumbnailUrl);

        // thêm hàm xóa thumbnail cũ nha fen

        quizRepository.save(quiz.get());

        return new ResponseSuccess<>(QuizConstants.CHANGE_QUIZ_THUMBNAIL,thumbnailUrl);
    }
}
