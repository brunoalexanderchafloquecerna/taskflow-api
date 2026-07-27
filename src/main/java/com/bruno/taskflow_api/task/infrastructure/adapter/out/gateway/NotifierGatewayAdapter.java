package com.bruno.taskflow_api.task.infrastructure.adapter.out.gateway;

import com.bruno.taskflow_api.notification.application.port.in.CreateNotificationUseCase;
import com.bruno.taskflow_api.task.application.port.out.NotifierGateway;
import org.springframework.stereotype.Component;

@Component
public class NotifierGatewayAdapter implements NotifierGateway {

  private final CreateNotificationUseCase createNotificationUseCase;

  public NotifierGatewayAdapter(CreateNotificationUseCase createNotificationUseCase) {
    this.createNotificationUseCase = createNotificationUseCase;
  }

  @Override
  public void notifyTaskCompleted(String taskId, String ownerId, String actorId) {
    createNotificationUseCase.createNotification(ownerId, "TASK_COMPLETED", actorId, taskId);
  }
}
