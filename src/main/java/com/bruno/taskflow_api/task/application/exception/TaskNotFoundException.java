package com.bruno.taskflow_api.task.application.exception;

import com.bruno.taskflow_api.shared.domain.exception.NotFoundException;
import java.util.UUID;

public class TaskNotFoundException extends NotFoundException {

  public TaskNotFoundException(UUID id) {
    super("The task with ID: " + id + " was not found.");
  }
}
