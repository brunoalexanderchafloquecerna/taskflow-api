package com.bruno.taskflow_api.notification.application.port.service;

import com.bruno.taskflow_api.notification.application.port.in.CreateNotificationUseCase;
import com.bruno.taskflow_api.notification.application.port.out.NotificationRepository;
import com.bruno.taskflow_api.notification.domain.model.NotificationEvent;
import org.springframework.stereotype.Service;

@Service
public class CreateNotificationService implements CreateNotificationUseCase {

  private final NotificationRepository notificationRepository;

  public CreateNotificationService(NotificationRepository notificationRepository) {
    this.notificationRepository = notificationRepository;
  }

  @Override
  public void createNotification(NotificationEvent notificationEvent) {
    notificationRepository.record(notificationEvent);
  }
}
