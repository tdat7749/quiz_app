package com.example.backend.modules.room.viewmodels;

import com.example.backend.modules.quiz.viewmodels.QuestionDetailVm;
import com.example.backend.modules.quiz.viewmodels.QuestionVm;
import com.example.backend.modules.quiz.viewmodels.QuizVm;
import com.example.backend.modules.user.viewmodels.UserVm;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoomRealTime {
    private int id;
    private UserVm host;

    @Nullable
    private String timeStart;

    @Nullable
    private String timeEnd;

    private QuizVm quiz;

    private String roomPin;

    private String createdAt;

    private String roomName;

    private GameModeVm mode;

    private boolean isClosed;
    private int maxUser;

    private List<QuestionDetailVm> questions;
    private List<UserVm> users;

    private long totalUser;
    private boolean isOnwer;
}
