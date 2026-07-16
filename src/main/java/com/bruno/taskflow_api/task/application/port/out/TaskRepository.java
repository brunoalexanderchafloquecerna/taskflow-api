package com.bruno.taskflow_api.task.application.port.out;

import com.bruno.taskflow_api.task.domain.model.Task;
import com.bruno.taskflow_api.task.domain.model.TaskStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {

  Task save(Task task);

  Optional<Task> findById(UUID id);

  List<Task> findTasksByFilters(UUID taskListId, TaskStatus status);

  void deleteById(UUID id);

  void deleteByTaskListId(UUID taskListId);
}
