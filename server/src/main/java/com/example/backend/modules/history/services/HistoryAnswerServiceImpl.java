package com.example.backend.modules.history.services;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.history.constant.HistoryConstants;
import com.example.backend.modules.history.dtos.CreateHistoryAnswerDTO;
import com.example.backend.modules.history.exceptions.HistoryNotFoundException;
import com.example.backend.modules.history.models.History;
import com.example.backend.modules.history.models.HistoryAnswer;
import com.example.backend.modules.history.repositories.HistoryAnswerRepository;
import com.example.backend.modules.history.viewmodels.HistoryAnswerVm;
import com.example.backend.modules.quiz.constant.QuizConstants;
import com.example.backend.modules.quiz.exceptions.QuestionNotFoundException;
import com.example.backend.modules.quiz.services.QuestionService;
import com.example.backend.modules.user.models.User;
import com.example.backend.utils.Utilities;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HistoryAnswerServiceImpl implements HistoryAnswerService{

    private final HistoryAnswerRepository historyAnswerRepository;

    private final HistoryService historyService;
    private final QuestionService questionService;

    public HistoryAnswerServiceImpl(
            HistoryAnswerRepository historyAnswerRepository,
            QuestionService questionService,
            @Lazy HistoryService historyService
    ){
        this.historyAnswerRepository = historyAnswerRepository;
        this.questionService = questionService;
        this.historyService = historyService;
    }

    @Override
    @Transactional
    public boolean createBulkHistoryAnswer(List<CreateHistoryAnswerDTO> listDto, History history) {
        List<HistoryAnswer> historyAnswerList = new ArrayList<>();

        for(CreateHistoryAnswerDTO item : listDto){
            var question = questionService.findById(item.getQuestionId());

            if(question.isEmpty()){
                throw new QuestionNotFoundException(QuizConstants.QUESTION_NOT_FOUND);
            }
            HistoryAnswer answer = HistoryAnswer.builder()
                    .createdAt(new Date())
                    .updatedAt(new Date())
                    .history(history)
                    .isCorrect(item.getIsCorrect())
                    .question(question.get())
                    .build();

            historyAnswerList.add(answer);
        }

        historyAnswerRepository.saveAll(historyAnswerList);

        return true;
    }

    @Override
    public ResponseSuccess<List<HistoryAnswerVm>> getListHistoryAnswer(User user, int historyId) {
        var history = historyService.findByIdAndUser(historyId,user);

        if(history.isEmpty()){
            throw new HistoryNotFoundException(HistoryConstants.HISTORY_NOT_FOUND);
        }

        var listHistoryAnswers = historyAnswerRepository.getListHistoryAnswer(history.get());

        var listHistoryAnswersVm = listHistoryAnswers.stream().map(Utilities::getHistoryAnswer).toList();

        return new ResponseSuccess<>("Thành công",listHistoryAnswersVm);
    }
}
