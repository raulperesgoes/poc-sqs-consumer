package com.example.poc.sqs.consumer.controller.kafka.producer;

import com.example.poc.sqs.consumer.dto.MessageDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaProducerController {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void producer(MessageDto messageDto) {
        this.kafkaTemplate.send("poc-sqs-consumer", messageDto);
    }
}
