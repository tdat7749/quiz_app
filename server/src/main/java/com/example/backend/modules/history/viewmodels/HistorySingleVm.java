package com.example.backend.modules.history.viewmodels;

import com.example.backend.modules.quiz.viewmodels.QuizVm;
import com.example.backend.modules.user.viewmodels.UserVm;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class HistorySingleVm {
    private int id;
    private UserVm user;
    private int totalCorrect;
    private String timeStart;
    private String timeEnd;
    private int totalScore;
    private QuizVm quiz;
}
