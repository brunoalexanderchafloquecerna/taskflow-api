package com.bruno.taskflow_api.workspace.domain.model;

import com.bruno.taskflow_api.workspace.domain.exception.InvalidWorkspaceException;
import java.util.UUID;

public class Workspace {

  private final UUID id;
  private String name;
  private final UUID userId;

  public Workspace(UUID id, String name, UUID userId) {
    this.id = id;
    this.name = name;
    this.userId = userId;
  }

  public static Workspace create(String name, UUID userId) {
    if (name == null || name.isBlank()) {
      throw new InvalidWorkspaceException("Workspace name cannot be blank");
    }
    return new Workspace(null, name, userId);
  }

  public void updateName(String name) {
    if (name != null && !name.trim().isBlank()) {
      this.name = name;
    }
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public UUID getUserId() {
    return userId;
  }
}
