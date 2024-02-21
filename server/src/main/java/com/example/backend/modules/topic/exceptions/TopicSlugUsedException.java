package com.example.backend.modules.topic.exceptions;

public class TopicSlugUsedException extends RuntimeException{
    public TopicSlugUsedException(String message){
        super(message);
    }
}
