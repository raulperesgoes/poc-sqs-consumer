package com.example.poc.sqs.consumer.application.integration.aws.queue.sqs.message;

import com.example.poc.sqs.consumer.controller.kafka.producer.KafkaProducerController;
import com.example.poc.sqs.consumer.dto.MessageDto;
import com.example.poc.sqs.consumer.entity.MessagesEntity;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SqsMessage {

    private final DynamoDbTemplate dynamoDbTemplate;

    public SqsMessage (DynamoDbTemplate dynamoDbTemplate) {
        this.dynamoDbTemplate = dynamoDbTemplate;
    }

    @Autowired
    private KafkaProducerController kafkaProducerController;

    @SqsListener("poc-sqs-consumer")
    public void listenMessageSqs(MessageDto messageDto) {
        System.out.println("Message received: " + messageDto.message() + " " + messageDto.sender());

        var entity = MessagesEntity.fromMessage(messageDto);
        dynamoDbTemplate.save(entity);
        kafkaProducerController.producer(messageDto);

        System.out.println("Message posted in the topic.");
    }
}
