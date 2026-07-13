package com.bruno.taskflow_api.task;

import com.bruno.taskflow_api.tasklist.TaskList;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Task {

  private UUID id;

  private String title;

  private String description;

  private TaskStatus status;

  private TaskList taskList;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  public Task(UUID id, String title, String description, TaskStatus status, TaskList taskList,
      LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.status = status;
    this.taskList = taskList;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public TaskStatus getStatus() {
    return status;
  }

  public void setStatus(TaskStatus status) {
    this.status = status;
  }

  public TaskList getTaskList() {
    return taskList;
  }

  public void setTaskList(TaskList taskList) {
    this.taskList = taskList;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Task task)) {
      return false;
    }
    return Objects.equals(id, task.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Task{id=%s, title='%s', description='%s', status=%s, taskList=%s, createdAt=%s, updatedAt=%s}".formatted(
        id, title, description, status, taskList, createdAt, updatedAt);
  }
}
