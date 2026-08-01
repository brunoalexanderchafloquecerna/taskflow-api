package com.bruno.taskflow_api.task.application.port.out;

import com.bruno.taskflow_api.task.application.event.TaskCompletedIntegrationEvent;

public interface TaskEventPublisher {

  void publishTaskCompleted(TaskCompletedIntegrationEvent event);
}
