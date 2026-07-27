package com.bruno.taskflow_api.notification.application.port.in;

import com.bruno.taskflow_api.notification.domain.model.NotificationEvent;

public interface CreateNotificationUseCase {

  void createNotification(NotificationEvent notificationEvent);
}
