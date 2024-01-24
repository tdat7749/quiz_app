package com.example.backend.modules.topic.repositories;

import com.example.backend.modules.topic.models.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TopicRepository extends JpaRepository<Topic,Integer> {

}
