package com.bruno.taskflow_api.notification.application.port.in;

public interface CreateNotificationUseCase {

  void createNotification(String userId, String eventType, String actorId, String taskId);
}
