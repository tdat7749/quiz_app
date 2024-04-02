package com.example.backend.modules.history.viewmodels;

import com.example.backend.modules.user.viewmodels.UserVm;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HistoryRankVm {
    private UserVm user;
    private int totalCorrect;
    private int totalScore;
}
