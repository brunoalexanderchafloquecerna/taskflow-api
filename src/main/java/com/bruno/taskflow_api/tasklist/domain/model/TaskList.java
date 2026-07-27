package com.bruno.taskflow_api.tasklist.domain.model;

import com.bruno.taskflow_api.tasklist.domain.exception.InvalidTaskListException;
import java.util.UUID;

public class TaskList {

  private final UUID id;
  private String name;
  private UUID workspaceId;
  private final UUID ownerId;
  private int position;

  public TaskList(UUID id, String name, UUID workspaceId, UUID ownerId, int position) {
    this.id = id;
    this.name = name;
    this.workspaceId = workspaceId;
    this.ownerId = ownerId;
    this.position = position;
  }

  public static TaskList create(String name, UUID workspaceId, UUID ownerId, int position) {
    if (name == null || name.isEmpty()) {
      throw new InvalidTaskListException("The TaskList name cannot be empty");
    }
    return new TaskList(null, name, workspaceId, ownerId, position);
  }

  public void updateInformation(String name, UUID workspaceId, int position) {
    if (this.name != null) {
      this.name = name;
    }
    if (this.workspaceId != null) {
      this.workspaceId = workspaceId;
    }
    this.position = position;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public UUID getWorkspaceId() {
    return workspaceId;
  }

  public int getPosition() {
    return position;
  }

  public UUID getOwnerId() {
    return ownerId;
  }
}
