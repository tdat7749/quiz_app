package com.example.backend.modules.room.viewmodels;

import com.example.backend.modules.quiz.viewmodels.QuizVm;
import com.example.backend.modules.user.viewmodels.UserVm;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;


@Data
@Builder
public class RoomVm {
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

    private boolean isClosed;
    private int maxUser;
    private boolean isPlayAgain;
    private long totalUser;
    private boolean isOnwer;
}
