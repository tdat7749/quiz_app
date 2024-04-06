package com.example.backend.utils;

import com.example.backend.modules.history.models.History;
import com.example.backend.modules.history.models.HistoryAnswer;
import com.example.backend.modules.history.viewmodels.*;
import com.example.backend.modules.quiz.models.Answer;
import com.example.backend.modules.quiz.models.Question;
import com.example.backend.modules.quiz.models.QuestionType;
import com.example.backend.modules.quiz.models.Quiz;
import com.example.backend.modules.quiz.viewmodels.*;
import com.example.backend.modules.room.models.Room;
import com.example.backend.modules.room.viewmodels.RoomVm;
import com.example.backend.modules.topic.models.Topic;
import com.example.backend.modules.topic.viewmodels.TopicVm;
import com.example.backend.modules.user.models.User;
import com.example.backend.modules.user.viewmodels.UserDetailVm;
import com.example.backend.modules.user.viewmodels.UserVm;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utilities {

    public static String generateCode() {
        Random rnd = new Random();
        int number = rnd.nextInt(9999999);

        return String.format("%06d", number);
    }

    public static UserVm getUserVm(User user){
        return UserVm.builder()
                .id(user.getId())
                .displayName(user.getDisplayName())
                .avatar(user.getAvatar())
                .build();
    }

    public static UserDetailVm getUserDetailVm(User user){
        return UserDetailVm.builder()
                .id(user.getId())
                .displayName(user.getDisplayName())
                .avatar(user.getAvatar())
                .email(user.getEmail())
                .role(user.getRole())
                .userName(user.getUsername())
                .build();
    }

    public static QuizVm getQuizVm(Quiz quiz){

        return QuizVm.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .slug(quiz.getSlug())
                .thumbnail(quiz.getThumbnail())
                .user(getUserVm(quiz.getUser()))
                .build();
    }

    public static QuizDetailVm getQuizDetailVm(Quiz quiz,boolean isCollect,boolean isOwner){

        // còn thiếu get cái totalScore của quiz
        return QuizDetailVm.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .slug(quiz.getSlug())
                .thumbnail(quiz.getThumbnail())
                .description(quiz.getDescription())
                .summary(quiz.getSummary())
                .updatedAt(quiz.getUpdatedAt().toString())
                .createdAt(quiz.getCreatedAt().toString())
                .user(getUserVm(quiz.getUser()))
                .topic(getTopicVm(quiz.getTopic()))
                .isCollect(isCollect)
                .isOwner(isOwner)
                .isPublic(quiz.getIsPublic())
                .build();
    }

    public static RoomVm getRoomVm(Room room){

        return RoomVm.builder()
                .roomPin(room.getRoomPin())
                .id(room.getId())
                .timeStart(room.getTimeStart() != null ? room.getTimeStart().toString() : null)
                .timeEnd(room.getTimeEnd() != null ? room.getTimeEnd().toString() : null)
                .createdAt(room.getCreatedAt().toString())
                .host(getUserVm(room.getUser()))
                .quiz(getQuizVm(room.getQuiz()))
                .roomName(room.getRoomName())
                .isClosed(room.isClosed())
                .build();
    }

    public static TopicVm getTopicVm(Topic topic){

        return TopicVm.builder()
                .id(topic.getId())
                .thumbnail(topic.getThumbnail())
                .slug(topic.getSlug())
                .title(topic.getTitle())
                .createdAt(topic.getCreatedAt().toString())
                .updatedAt(topic.getUpdatedAt().toString())
                .build();
    }

    public static HistoryRoomVm getHistoryRoomVm(History history){

        return HistoryRoomVm.builder()
                .id(history.getId())
                .timeStart(history.getStartedAt().toString())
                .timeEnd(history.getFinishedAt().toString())
                .room(getRoomVm(history.getRoom()))
                .user(getUserVm(history.getUser()))
                .totalScore(history.getScore())
                .totalCorrect(history.getTotalCorrect())
                .build();
    }

    public static HistorySingleVm getHistorySingleVm(History history){

        return HistorySingleVm.builder()
                .id(history.getId())
                .timeStart(history.getStartedAt().toString())
                .timeEnd(history.getFinishedAt().toString())
                .quiz(getQuizVm(history.getQuiz()))
                .user(getUserVm(history.getUser()))
                .totalScore(history.getScore())
                .totalCorrect(history.getTotalCorrect())
                .build();
    }

    public static AnswerVm getAnswerVm(Answer answer){

        return AnswerVm.builder()
                .id(answer.getId())
                .title(answer.getTitle())
                .isCorrect(answer.getIsCorrect())
                .build();
    }

    public static List<AnswerVm> getListAnswerVm(List<Answer> answers){
        List<AnswerVm> answerVmList = new ArrayList<>();

        for(Answer answer : answers){
            AnswerVm answerVm = AnswerVm.builder()
                    .id(answer.getId())
                    .title(answer.getTitle())
                    .isCorrect(answer.getIsCorrect())
                    .build();

            answerVmList.add(answerVm);
        }

        return answerVmList;
    }

    public static QuestionVm getQuestionVm(Question question){

        return QuestionVm.builder()
                .score(question.getScore())
                .id(question.getId())
                .title(question.getTitle())
                .answers(getListAnswerVm(question.getAnswers()))
                .build();
    }

    public static QuestionDetailVm getQuestionDetailVm(Question question){

        return QuestionDetailVm.builder()
                .score(question.getScore())
                .id(question.getId())
                .title(question.getTitle())
                .answers(getListAnswerVm(question.getAnswers()))
                .order(question.getOrder())
                .timeLimit(question.getTimeLimit())
                .thumbnail(question.getThumbnail())
                .questionType(getQuestionTypeVm(question.getQuestionType()))
                .build();
    }

    public static QuestionTypeVm getQuestionTypeVm(QuestionType questionType){

        return QuestionTypeVm.builder()
                .id(questionType.getId())
                .title(questionType.getTitle())
                .build();
    }

    public static HistoryAnswerVm getHistoryAnswer(HistoryAnswer historyAnswer){

        return HistoryAnswerVm.builder()
                .id(historyAnswer.getId())
                .isCorrect(historyAnswer.getIsCorrect())
                .question(getQuestionVm(historyAnswer.getQuestion()))
                .build();
    }

    public static HistoryRankVm getHistoryRankVm(HistoryRank historyRank){
        return HistoryRankVm.builder()
                .user(getUserVm(historyRank.getUser()))
                .totalCorrect(historyRank.getTotalCorrect())
                .totalScore(historyRank.getTotalScore())
                .build();
    }
}

