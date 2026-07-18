package com.bruno.taskflow_api.task.infrastructure.adapter.out.gateway;

import com.bruno.taskflow_api.task.application.port.out.TaskListGateway;
import com.bruno.taskflow_api.tasklist.application.port.in.TaskListUseCase;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class TaskListGatewayAdapter implements TaskListGateway {

  private final TaskListUseCase taskListUseCase;

  public TaskListGatewayAdapter(TaskListUseCase taskListUseCase) {
    this.taskListUseCase = taskListUseCase;
  }

  @Override
  public boolean taskListExists(UUID taskListId) {
    return taskListUseCase.existsTaskListById(taskListId);
  }
}
