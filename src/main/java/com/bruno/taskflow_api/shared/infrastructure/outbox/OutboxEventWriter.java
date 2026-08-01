package com.bruno.taskflow_api.shared.infrastructure.outbox;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
public class OutboxEventWriter {

  private static final Logger LOGGER = LoggerFactory.getLogger(OutboxEventWriter.class);

  private final OutboxEventRepository outboxEventRepository;

  private final JsonMapper jsonMapper;

  public OutboxEventWriter(OutboxEventRepository outboxEventRepository, JsonMapper jsonMapper) {
    this.outboxEventRepository = outboxEventRepository;
    this.jsonMapper = jsonMapper;
  }

  public void write(String aggregateType, UUID aggregateId, String eventType, Object payload) {
    String json = jsonMapper.writeValueAsString(payload);
    outboxEventRepository.save(OutboxEvent.of(aggregateType, aggregateId, eventType, json));
    LOGGER.info("Writing outbox event: type={}, aggregateId={}", eventType, aggregateId);
  }
}
