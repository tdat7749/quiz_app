package com.example.backend.utils;

import com.example.backend.modules.quiz.models.Quiz;
import com.example.backend.modules.quiz.viewmodels.QuizVm;
import com.example.backend.modules.room.models.Room;
import com.example.backend.modules.room.viewmodels.RoomVm;
import com.example.backend.modules.user.models.User;
import com.example.backend.modules.user.viewmodels.UserVm;

import java.util.Random;

public class Utilities {
    public static String generateCode() {
        Random rnd = new Random();
        int number = rnd.nextInt(9999999);

        return String.format("%06d", number);
    }

    public static UserVm getUserVm(User user){
        UserVm userVm = UserVm.builder()
                .id(user.getId())
                .displayName(user.getDisplayName())
                .avatar(user.getAvatar())
                .build();
        return userVm;
    }

    public static QuizVm getQuizVm(Quiz quiz){
        QuizVm quizVm = QuizVm.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .slug(quiz.getSlug())
                .thumbnail(quiz.getThumbnail())
                .build();

        return quizVm;
    }

    public static RoomVm getRoomVm(Room room){
        RoomVm roomVm = RoomVm.builder()
                .roomPin(room.getRoomPin())
                .id(room.getId())
                .timeStart(room.getTimeStart().toString())
                .timeEnd(room.getTimeEnd().toString())
                .createdAt(room.getCreatedAt().toString())
                .host(getUserVm(room.getUser()))
                .quiz(getQuizVm(room.getQuiz()))
                .build();

        return roomVm;
    }
}

