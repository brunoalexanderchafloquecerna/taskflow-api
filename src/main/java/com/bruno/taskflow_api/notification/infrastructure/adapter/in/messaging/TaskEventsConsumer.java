package com.bruno.taskflow_api.notification.infrastructure.adapter.in.messaging;

import com.bruno.taskflow_api.notification.application.port.event.TaskCompletedEvent;
import com.bruno.taskflow_api.notification.application.port.in.CreateNotificationUseCase;
import java.nio.charset.StandardCharsets;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
public class TaskEventsConsumer {

  private static final Logger LOGGER = LoggerFactory.getLogger(TaskEventsConsumer.class);

  private final JsonMapper jsonMapper;

  private final CreateNotificationUseCase createNotificationUseCase;

  public TaskEventsConsumer(JsonMapper jsonMapper,
      CreateNotificationUseCase createNotificationUseCase) {
    this.jsonMapper = jsonMapper;
    this.createNotificationUseCase = createNotificationUseCase;
  }

  @KafkaListener(topics = "task-events", groupId = "notification-service")
  public void onMessage(ConsumerRecord<String, String> record) {
    String eventType = headerAsString(record);
    LOGGER.info("Evento recibido: type={}, key={}, offset={}", eventType, record.key(),
        record.offset());

    if ("TASK_COMPLETED".equals(eventType)) {
      TaskCompletedEvent event = jsonMapper.readValue(record.value(), TaskCompletedEvent.class);
      createNotificationUseCase.createNotification(event.ownerId().toString(), eventType,
          event.completedBy().toString(), event.taskId().toString());
    } else {
      LOGGER.warn("Tipo de evento desconocido, ignorado: {}", eventType);
    }
  }

  private String headerAsString(ConsumerRecord<String, String> record) {
    Header header = record.headers().lastHeader("event_type");
    return header != null ? new String(header.value(), StandardCharsets.UTF_8) : null;
  }
}
