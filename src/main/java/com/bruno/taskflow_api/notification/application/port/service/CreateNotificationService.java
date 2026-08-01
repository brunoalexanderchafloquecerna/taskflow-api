package com.bruno.taskflow_api.notification.application.port.service;

import com.bruno.taskflow_api.notification.application.port.in.CreateNotificationUseCase;
import com.bruno.taskflow_api.notification.application.port.out.NotificationRepository;
import com.bruno.taskflow_api.notification.domain.model.NotificationEvent;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class CreateNotificationService implements CreateNotificationUseCase {

  private final NotificationRepository notificationRepository;

  public CreateNotificationService(NotificationRepository notificationRepository) {
    this.notificationRepository = notificationRepository;
  }

  @Override
  @CircuitBreaker(name = "notificationRepository")
  public void createNotification(String userId, String eventType, String actorId, String taskId) {
    notificationRepository.record(new NotificationEvent(userId, eventType, actorId, Instant.now(),
        "{\"taskId\": \"" + taskId + "\"}", Instant.now().plusSeconds(2_592_000)));
  }
}
