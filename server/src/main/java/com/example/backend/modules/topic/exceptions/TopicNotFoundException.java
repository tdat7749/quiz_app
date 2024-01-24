package com.example.backend.modules.topic.exceptions;

public class TopicNotFoundException extends RuntimeException {
    public TopicNotFoundException(String message){
        super(message);
    }
}
