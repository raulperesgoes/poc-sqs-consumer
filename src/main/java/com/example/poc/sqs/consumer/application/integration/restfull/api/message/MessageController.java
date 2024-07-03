package com.example.poc.sqs.consumer.application.integration.restfull.api.message;

import com.example.poc.sqs.consumer.dto.MessageDto;
import com.example.poc.sqs.consumer.entity.MessagesEntity;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class MessageController {

    private final DynamoDbTemplate dynamoDbTemplate;

    public MessageController (DynamoDbTemplate dynamoDbTemplate) {
        this.dynamoDbTemplate = dynamoDbTemplate;
    }

    @PostMapping("/messages")
    public ResponseEntity<Void> save(@RequestBody MessageDto messageDto) {

        var entity = MessagesEntity.fromMessage(messageDto);
        dynamoDbTemplate.save(entity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{sender}/messages")
    public ResponseEntity<List<MessagesEntity>> getAllMessagesBySender(@PathVariable("sender") String sender) {

        var key = Key.builder().partitionValue(sender).build();

        var condition = QueryConditional.keyEqualTo(key);

        var query = QueryEnhancedRequest.builder()
                .queryConditional(condition)
                .build();

        var messages = dynamoDbTemplate.query(query, MessagesEntity.class);

        return ResponseEntity.ok(messages.items().stream().toList());
    }

    @GetMapping("{sender}/messages/{messageId}")
    public ResponseEntity<MessagesEntity> getSenderMessage(@PathVariable("sender") String sender,
                                                           @PathVariable("messageId") String messageId) {

        var entity = dynamoDbTemplate.load(Key.builder()
                .partitionValue(sender)
                .sortValue(messageId)
                .build(), MessagesEntity.class);

        return entity == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(entity);
    }

    @PutMapping("/{sender}/messages/{messageId}")
    public ResponseEntity<Void> updateMessage(@PathVariable("sender") String sender,
                                              @PathVariable("messageId") String messageId,
                                              @RequestBody MessageDto messageDto) {

        var entity = dynamoDbTemplate.load(Key.builder()
                .partitionValue(sender)
                .sortValue(messageId)
                .build(), MessagesEntity.class);

        if (entity == null) {
            return ResponseEntity.notFound().build();
        }

        entity.setMessage(messageDto.message());
        entity.setUpdatedAt(Instant.now());

        dynamoDbTemplate.save(entity);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{sender}/messages/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable("sender") String sender,
                                              @PathVariable("messageId") String messageId) {

        var entity = dynamoDbTemplate.load(Key.builder()
                .partitionValue(sender)
                .sortValue(messageId)
                .build(), MessagesEntity.class);

        if (entity == null) {
            return ResponseEntity.notFound().build();
        }

        dynamoDbTemplate.delete(entity);

        return ResponseEntity.ok().build();
    }
}
