package com.bruno.taskflow_api.tasklist;

import com.bruno.taskflow_api.NotFoundException;
import java.util.UUID;

public class TaskListNotFoundException extends NotFoundException {

  public TaskListNotFoundException(UUID uuid) {
    super("Task list with ID: " + uuid + " was not found.");
  }
}
