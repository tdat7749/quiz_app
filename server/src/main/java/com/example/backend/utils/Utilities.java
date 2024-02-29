package com.example.backend.utils;

import com.example.backend.modules.history.models.History;
import com.example.backend.modules.history.models.HistoryAnswer;
import com.example.backend.modules.history.viewmodels.HistoryAnswerVm;
import com.example.backend.modules.history.viewmodels.HistoryRoomVm;
import com.example.backend.modules.history.viewmodels.HistorySingleVm;
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
                .user(getUserVm(quiz.getUser()))
                .build();

        return quizVm;
    }

    public static QuizDetailVm getQuizDetailVm(Quiz quiz){
        QuizDetailVm quizDetailVm = QuizDetailVm.builder()
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
                .build();

        // còn thiếu get cái totalScore của quiz
        return quizDetailVm;
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

    public static TopicVm getTopicVm(Topic topic){
        TopicVm topicVm = TopicVm.builder()
                .id(topic.getId())
                .thumbnail(topic.getThumbnail())
                .slug(topic.getSlug())
                .title(topic.getTitle())
                .createdAt(topic.getCreatedAt().toString())
                .updatedAt(topic.getUpdatedAt().toString())
                .build();

        return topicVm;
    }

    public static HistoryRoomVm getHistoryRoomVm(History history){
        HistoryRoomVm historyRoomVm = HistoryRoomVm.builder()
                .id(history.getId())
                .timeStart(history.getStartedAt().toString())
                .timeEnd(history.getFinishedAt().toString())
                .room(getRoomVm(history.getRoom()))
                .user(getUserVm(history.getUser()))
                .totalScore(history.getScore())
                .totalCorrect(history.getTotalCorrect())
                .build();

        return historyRoomVm;
    }

    public static HistorySingleVm getHistorySingleVm(History history){
        HistorySingleVm historySingleVm = HistorySingleVm.builder()
                .id(history.getId())
                .timeStart(history.getStartedAt().toString())
                .timeEnd(history.getFinishedAt().toString())
                .quiz(getQuizVm(history.getQuiz()))
                .user(getUserVm(history.getUser()))
                .totalScore(history.getScore())
                .totalCorrect(history.getTotalCorrect())
                .build();

        return historySingleVm;
    }

    public static AnswerVm getAnswerVm(Answer answer){
        AnswerVm answerVm = AnswerVm.builder()
                .id(answer.getId())
                .title(answer.getTitle())
                .isCorrect(answer.getIsCorrect())
                .build();

        return answerVm;
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
        QuestionVm questionVm = QuestionVm.builder()
                .score(question.getScore())
                .id(question.getId())
                .title(question.getTitle())
                .answers(getListAnswerVm(question.getAnswers()))
                .build();

        return questionVm;
    }

    public static QuestionDetailVm getQuestionDetailVm(Question question){
        QuestionDetailVm questionDetailVm = QuestionDetailVm.builder()
                .score(question.getScore())
                .id(question.getId())
                .title(question.getTitle())
                .answers(getListAnswerVm(question.getAnswers()))
                .order(question.getOrder())
                .timeLimit(question.getTimeLimit())
                .thumbnail(question.getThumbnail())
                .questionType(getQuestionTypeVm(question.getQuestionType()))
                .build();

        return questionDetailVm;
    }

    public static QuestionTypeVm getQuestionTypeVm(QuestionType questionType){
        QuestionTypeVm questionTypeVm = QuestionTypeVm.builder()
                .id(questionType.getId())
                .title(questionType.getTitle())
                .build();

        return  questionTypeVm;
    }

    public static HistoryAnswerVm getHistoryAnswer(HistoryAnswer historyAnswer){
        HistoryAnswerVm historyAnswerVm = HistoryAnswerVm.builder()
                .id(historyAnswer.getId())
                .isCorrect(historyAnswer.getIsCorrect())
                .question(getQuestionVm(historyAnswer.getQuestion()))
                .build();

        return  historyAnswerVm;
    }
}

