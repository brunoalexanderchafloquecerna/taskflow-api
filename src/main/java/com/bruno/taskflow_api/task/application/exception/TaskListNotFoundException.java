package com.bruno.taskflow_api.task.application.exception;

import com.bruno.taskflow_api.shared.domain.exception.NotFoundException;
import java.util.UUID;

public class TaskListNotFoundException extends NotFoundException {

  public TaskListNotFoundException(UUID id) {
    super("The task list with ID: " + id + " was not found.");
  }
}
