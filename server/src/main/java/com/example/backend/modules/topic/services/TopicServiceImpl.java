package com.example.backend.modules.topic.services;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.topic.dtos.CreateTopicDTO;
import com.example.backend.modules.topic.dtos.EditTopicDTO;
import com.example.backend.modules.topic.viewmodels.TopicVm;

import java.util.List;

public class TopicServiceImpl implements TopicService{
    @Override
    public ResponseSuccess<List<TopicVm>> getAllTopic() {
        return null;
    }

    @Override
    public ResponseSuccess<TopicVm> createTopic(CreateTopicDTO dto) {
        return null;
    }

    @Override
    public ResponseSuccess<TopicVm> editTopic(EditTopicDTO dto) {
        return null;
    }

    @Override
    public ResponseSuccess<Boolean> deleteTopic(int topicId) {
        return null;
    }
}
