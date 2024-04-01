package com.example.backend.modules.topic.services;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.filestorage.services.FileStorageService;
import com.example.backend.modules.topic.constant.TopicConstants;
import com.example.backend.modules.topic.dtos.CreateTopicDTO;
import com.example.backend.modules.topic.dtos.EditTopicDTO;
import com.example.backend.modules.topic.exceptions.TopicNotFoundException;
import com.example.backend.modules.topic.exceptions.TopicSlugUsedException;
import com.example.backend.modules.topic.models.Topic;
import com.example.backend.modules.topic.repositories.TopicRepository;
import com.example.backend.modules.topic.viewmodels.TopicVm;
import com.example.backend.utils.Utilities;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TopicServiceImpl implements TopicService{
    private final TopicRepository topicRepository;
    private final FileStorageService fileStorageService;

    public TopicServiceImpl(
            TopicRepository topicRepository,
            FileStorageService fileStorageService
    ){
        this.topicRepository = topicRepository;
        this.fileStorageService = fileStorageService;
    }



    @Override
    @Transactional
    public ResponseSuccess<TopicVm> createTopic(CreateTopicDTO dto) throws IOException {

        var topic = topicRepository.findBySlug(dto.getSlug());
        if(topic.isPresent()){
            throw new TopicSlugUsedException(TopicConstants.TOPIC_SLUG_USED);
        }
        String thumbnailUrl = fileStorageService.uploadFile(dto.getThumbnail());

        Topic newTopic = Topic.builder()
                .title(dto.getTitle())
                .createdAt(new Date())
                .updatedAt(new Date())
                .thumbnail(thumbnailUrl)
                .slug(dto.getSlug())
                .build();

        var save = topicRepository.save(newTopic);

        var topicVm = Utilities.getTopicVm(save);

        return new ResponseSuccess<>(TopicConstants.CREATE_TOPIC,topicVm);
    }
    @Override
    public ResponseSuccess<List<TopicVm>> getAllTopic() {
        var listTopics = topicRepository.findAll();
        var result = listTopics.stream().map(Utilities::getTopicVm).toList();

        return new ResponseSuccess<>("Thành công",result);
    }

    @Override
    public Optional<Topic> findById(int topicId) {
        return topicRepository.findById(topicId);
    }


    @Override
    public ResponseSuccess<Boolean> deleteTopic(int topicId) {
        var topic = topicRepository.findById(topicId);
        if(topic.isEmpty()){
            throw new TopicNotFoundException(TopicConstants.TOPIC_NOT_FOUND);
        }

        topicRepository.delete(topic.get());

        return new ResponseSuccess<>(TopicConstants.DELETE_TOPIC,true);
    }

    @Override
    public ResponseSuccess<TopicVm> editTopic(EditTopicDTO dto) throws IOException {
        var topic = topicRepository.findById(dto.getTopicId());
        if(topic.isEmpty()){
            throw new TopicNotFoundException(TopicConstants.TOPIC_NOT_FOUND);
        }

        var topicFoundBySlug = topicRepository.findBySlug(dto.getSlug());

        if(topicFoundBySlug.isPresent() && !topic.get().equals(topicFoundBySlug.get())){
            throw new TopicSlugUsedException(TopicConstants.TOPIC_SLUG_USED);
        }

        String thumbnailUrl = null;
        if(dto.getThumbnail() != null){
            thumbnailUrl = fileStorageService.uploadFile(dto.getThumbnail());
        }

        topic.get()
                .setUpdatedAt(new Date());
        topic.get()
                .setSlug(dto.getSlug());
        topic.get()
                .setTitle(dto.getTitle());
        topic.get()
                .setUpdatedAt(new Date());
        if(thumbnailUrl != null){
            topic.get().setThumbnail(thumbnailUrl);
        }

        var save = topicRepository.save(topic.get());
        var result = Utilities.getTopicVm(save);

        return new ResponseSuccess<>(TopicConstants.EDIT_TOPIC,result);

    }
}
