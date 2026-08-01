package com.bruno.taskflow_api.shared.infrastructure.outbox;

import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OutboxRelay {

  private static final Logger LOG = LoggerFactory.getLogger(OutboxRelay.class);

  private static final int BATCH_SIZE = 50;

  private final OutboxEventRepository outboxEventRepository;

  private final KafkaEventPublisher kafkaEventPublisher;

  public OutboxRelay(OutboxEventRepository outboxEventRepository,
      KafkaEventPublisher kafkaEventPublisher) {
    this.outboxEventRepository = outboxEventRepository;
    this.kafkaEventPublisher = kafkaEventPublisher;
  }

  @Scheduled(fixedDelay = 1000)
  @Transactional
  public void publishPendingEvents() {
    List<OutboxEvent> pendingEvents = outboxEventRepository.lockNextPendingBatch(BATCH_SIZE);
    if (pendingEvents.isEmpty()) {
      LOG.info("No hay outbox events pendientes");
      return;
    }
    LOG.info("Procesando {} outbox event(s) pendiente(s)", pendingEvents.size());
    for (OutboxEvent event : pendingEvents) {
      try {
        LOG.info("Publishing event: {}", event);
        ProducerRecord<String, String> record = new ProducerRecord<>(
            topicFor(event.getAggregateType()), event.getAggregateId().toString(),
            event.getPayload());
        record.headers().add("event_type", event.getEventType().getBytes(StandardCharsets.UTF_8));
        kafkaEventPublisher.publishEvent(record);
        event.markAsProcessed();
      } catch (Exception e) {
        LOG.error("Fallo publicando outbox event {} (se reintenta en el próximo ciclo)",
            event.getId(), e);
      }
    }

  }

  private String topicFor(String aggregateType) {
    return aggregateType.toLowerCase() + "-events";
  }
}
