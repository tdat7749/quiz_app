package com.example.backend.modules.history.viewmodels;


import com.example.backend.modules.user.models.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HistoryRank {
    private User user;
    private int totalCorrect;
    private int totalScore;
}
