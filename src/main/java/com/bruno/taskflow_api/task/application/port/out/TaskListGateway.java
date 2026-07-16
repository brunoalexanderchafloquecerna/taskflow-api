package com.bruno.taskflow_api.task.application.port.out;

import java.util.UUID;

public interface TaskListGateway {

  boolean taskListExists(UUID taskListId);
}
