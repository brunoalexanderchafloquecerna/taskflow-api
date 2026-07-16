package com.bruno.taskflow_api.tasklist.application.exception;

import com.bruno.taskflow_api.shared.domain.exception.NotFoundException;
import java.util.UUID;

public class TaskListNotFoundException extends NotFoundException {

  public TaskListNotFoundException(UUID id) {
    super("Task List not found with ID: " + id);
  }
}
