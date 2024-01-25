package com.example.backend.modules.topic.services;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.topic.dtos.CreateTopicDTO;
import com.example.backend.modules.topic.dtos.EditTopicDTO;
import com.example.backend.modules.topic.viewmodels.TopicVm;
import com.example.backend.modules.user.models.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface TopicService {
    ResponseSuccess<List<TopicVm>> getAllTopic();

    ResponseSuccess<TopicVm> createTopic(CreateTopicDTO dto) throws IOException;

    ResponseSuccess<TopicVm> editTopic(EditTopicDTO dto) throws IOException;

    ResponseSuccess<Boolean> deleteTopic(int topicId);
}
