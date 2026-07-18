package com.bruno.taskflow_api.tasklist.application.exception;

import com.bruno.taskflow_api.shared.domain.exception.NotFoundException;
import java.util.UUID;

public class TaskListNotFoundException extends NotFoundException {

  public TaskListNotFoundException(UUID uuid) {
    super("Task list with ID: " + uuid + " was not found.");
  }
}
