package com.bruno.taskflow_api.task;

import com.bruno.taskflow_api.NotFoundException;
import java.util.UUID;

public class TaskNotFoundException extends NotFoundException {

  public TaskNotFoundException(UUID id) {
    super("Task not found with ID: " + id);
  }
}
