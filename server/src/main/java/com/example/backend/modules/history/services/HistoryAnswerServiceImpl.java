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
import com.example.backend.modules.quiz.exceptions.AnswerNotFoundException;
import com.example.backend.modules.quiz.exceptions.QuestionNotFoundException;
import com.example.backend.modules.quiz.services.AnswerService;
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
    private final AnswerService answerService;

    public HistoryAnswerServiceImpl(
            HistoryAnswerRepository historyAnswerRepository,
            QuestionService questionService,
            @Lazy HistoryService historyService,
            AnswerService answerService
    ){
        this.historyAnswerRepository = historyAnswerRepository;
        this.questionService = questionService;
        this.historyService = historyService;
        this.answerService = answerService;
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


            if(item.getAnswerId() != null){
                var findAnswer = answerService.findById(item.getAnswerId());

                if(findAnswer.isEmpty()){
                    throw new AnswerNotFoundException(QuizConstants.ANSWER_NOT_FOUND);
                }

                HistoryAnswer answer = HistoryAnswer.builder()
                        .createdAt(new Date())
                        .updatedAt(new Date())
                        .history(history)
                        .isCorrect(item.getIsCorrect())
                        .question(question.get())
                        .answer(findAnswer.get())
                        .build();

                historyAnswerList.add(answer);
            }else{
                HistoryAnswer answer = HistoryAnswer.builder()
                        .createdAt(new Date())
                        .updatedAt(new Date())
                        .history(history)
                        .isCorrect(item.getIsCorrect())
                        .question(question.get())
                        .answer(null)
                        .build();

                historyAnswerList.add(answer);
            }
        }

        historyAnswerRepository.saveAll(historyAnswerList);

        return true;
    }

    @Override
    public void deleteByHistoryId(int historyId) {
        historyAnswerRepository.deleteByHistoryId(historyId);
    }

}
