package com.bruno.taskflow_api.task.domain.model;

import com.bruno.taskflow_api.task.domain.exception.InvalidTaskException;
import com.bruno.taskflow_api.task.domain.exception.InvalidTaskTransitionException;
import java.time.LocalDateTime;
import java.util.UUID;

public class Task {

  private final UUID id;
  private String title;
  private String description;
  private TaskStatus status;
  private UUID taskListId;
  private final UUID ownerId;
  private LocalDateTime updatedAt;
  private final LocalDateTime createdAt;

  public Task(UUID id, String title, String description, TaskStatus status, UUID taskListId,
      UUID ownerId, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.status = status;
    this.taskListId = taskListId;
    this.ownerId = ownerId;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public static Task create(String title, String description, UUID taskListId, UUID ownerId) {
    validaTitle(title);
    LocalDateTime now = LocalDateTime.now();
    return new Task(null, title, description, TaskStatus.TODO, taskListId, ownerId, now, now);
  }

  private static void validaTitle(String title) {
    if (title == null || title.isEmpty()) {
      throw new InvalidTaskException("The task title cannot be empty");
    }
  }

  public void moveTo(TaskStatus newStatus) {
    if (this.status == TaskStatus.DONE && newStatus == TaskStatus.TODO) {
      throw new InvalidTaskTransitionException("You can't move a task from DONE to TODO directly");
    }
    this.status = newStatus;
    this.updatedAt = LocalDateTime.now();
  }

  public void updateInformation(String newTitle, String newDescription, TaskStatus newStatus,
      UUID newTaskListId) {
    if (newTitle != null) {
      validaTitle(newTitle);
      this.title = newTitle;
    }
    if (newDescription != null) {
      this.description = newDescription;
    }
    if (newStatus != null) {
      this.status = newStatus;
    }
    if (newTaskListId != null) {
      this.taskListId = newTaskListId;
    }
    this.updatedAt = LocalDateTime.now();
  }

  public UUID getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public TaskStatus getStatus() {
    return status;
  }

  public UUID getTaskListId() {
    return taskListId;
  }

  public UUID getOwnerId() {
    return ownerId;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }
}
