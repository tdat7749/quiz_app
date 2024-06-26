package com.example.backend.modules.history.viewmodels;


import com.example.backend.modules.quiz.viewmodels.AnswerVm;
import com.example.backend.modules.quiz.viewmodels.QuestionDetailVm;
import com.example.backend.modules.quiz.viewmodels.QuestionVm;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HistoryAnswerVm {
    private int id;
    private boolean isCorrect;
    private QuestionVm question;

    @Nullable
    private AnswerVm answer;
}
