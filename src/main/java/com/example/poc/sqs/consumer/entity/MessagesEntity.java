package com.example.poc.sqs.consumer.entity;

import com.example.poc.sqs.consumer.dto.MessageDto;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.Instant;
import java.util.UUID;

@DynamoDbBean
public class MessagesEntity {

    private UUID messageId;
    private String sender;
    private String message;
    private Instant createdAt;
    private Instant updatedAt;

    public static MessagesEntity fromMessage(MessageDto messageDto) {
        var entity = new MessagesEntity();

        entity.setMessage(messageDto.message());
        entity.setMessageId(UUID.randomUUID());
        entity.setCreatedAt(Instant.now());
        entity.setSender(messageDto.sender());

        return entity;
    }

    @DynamoDbSortKey
    public UUID getMessageId() {
        return messageId;
    }

    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @DynamoDbPartitionKey
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
