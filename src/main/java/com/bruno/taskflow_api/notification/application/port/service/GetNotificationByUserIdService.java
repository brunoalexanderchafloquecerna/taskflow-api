package com.bruno.taskflow_api.notification.application.port.service;

import com.bruno.taskflow_api.notification.application.port.in.GetNotificationByUserIdUseCase;
import com.bruno.taskflow_api.notification.application.port.out.NotificationRepository;
import com.bruno.taskflow_api.notification.domain.model.NotificationEvent;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GetNotificationByUserIdService implements GetNotificationByUserIdUseCase {

  private final NotificationRepository notificationRepository;

  public GetNotificationByUserIdService(NotificationRepository notificationRepository) {
    this.notificationRepository = notificationRepository;
  }

  @Override
  public List<NotificationEvent> findByUserId(String userId) {
    return notificationRepository.findByUserId(userId);
  }
}
