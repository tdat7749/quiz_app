package com.example.backend.modules.room.viewmodels;

import com.example.backend.modules.quiz.viewmodels.QuizVm;
import com.example.backend.modules.user.viewmodels.UserVm;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class RoomVm {
    private int id;
    private UserVm host;

    private String timeStart;

    private String timeEnd;

    private QuizVm quiz;

    private String roomPin;

    private String createdAt;

}
