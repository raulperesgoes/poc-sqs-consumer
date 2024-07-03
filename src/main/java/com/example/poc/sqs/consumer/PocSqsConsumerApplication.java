package com.example.poc.sqs.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class PocSqsConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PocSqsConsumerApplication.class, args);
	}

}
