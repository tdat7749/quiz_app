package com.example.backend.modules.topic.controllers;

import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.topic.dtos.CreateTopicDTO;
import com.example.backend.modules.topic.dtos.EditTopicDTO;
import com.example.backend.modules.topic.services.TopicService;
import com.example.backend.modules.topic.viewmodels.TopicVm;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/topics")
public class TopicController {
    private TopicService topicService;

    public TopicController(
            TopicService topicService
    ){
        this.topicService = topicService;
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<List<TopicVm>>> getAllTopic(){
        var result = topicService.getAllTopic();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<TopicVm>> createTopic(
            @ModelAttribute @Valid CreateTopicDTO dto
    ) throws IOException {
        var result = topicService.createTopic(dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PatchMapping("/")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<TopicVm>> editTopic(
            @ModelAttribute @Valid EditTopicDTO dto
    ) throws IOException {
        var result = topicService.editTopic(dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{topicId}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> deleteTopic(
            @PathVariable("topicId") int topicId
    ){
        var result = topicService.deleteTopic(topicId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
