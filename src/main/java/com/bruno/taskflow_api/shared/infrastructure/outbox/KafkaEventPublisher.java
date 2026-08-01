package com.bruno.taskflow_api.shared.infrastructure.outbox;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventPublisher {

  private final KafkaTemplate<String, String> kafkaTemplate;

  public KafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @CircuitBreaker(name = "kafkaPublisher")
  public void publishEvent(ProducerRecord<String, String> record)
      throws ExecutionException, InterruptedException {
    kafkaTemplate.send(record).get();
  }
}
