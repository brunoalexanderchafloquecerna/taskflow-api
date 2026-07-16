package com.bruno.taskflow_api.task.application.port.in;

import com.bruno.taskflow_api.task.domain.model.Task;
import com.bruno.taskflow_api.task.domain.model.TaskStatus;
import java.util.List;
import java.util.UUID;

public interface TaskUseCase {

  Task createTask(String title, String description, UUID taskListId);

  Task changeTaskStatus(UUID id, TaskStatus status);

  Task updateTaskInformation(UUID id, String title, String description, TaskStatus status, UUID taskListId);

  Task getTaskById(UUID id);

  List<Task> getTasksByFilters(UUID taskListId, TaskStatus status);

  void deleteTask(UUID id);

  void deleteTasksByTaskListId(UUID taskListId);
}
