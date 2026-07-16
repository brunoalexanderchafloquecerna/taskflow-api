package com.bruno.taskflow_api.tasklist.application.port.in;

import com.bruno.taskflow_api.tasklist.domain.model.TaskList;
import java.util.List;
import java.util.UUID;

public interface TaskListUseCase {

  TaskList createTaskList(String name, UUID workspaceId, int position);

  TaskList getTaskListById(UUID id);

  List<TaskList> getTaskListsByFilters(UUID workspaceId);

  boolean existsTaskListById(UUID id);

  TaskList updateTaskListById(UUID id, String name, UUID workspaceId, int position);

  void deleteTaskListById(UUID id);
}
