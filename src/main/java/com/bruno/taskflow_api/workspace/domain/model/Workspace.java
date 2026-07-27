package com.bruno.taskflow_api.workspace.domain.model;

import com.bruno.taskflow_api.workspace.domain.exception.InvalidWorkspaceException;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class Workspace implements Serializable {

  @Serial
  private static final long serialVersionUID = -4876012013953725523L;

  private final UUID id;
  private final UUID ownerId;
  private String name;

  public Workspace(UUID id, String name, UUID ownerId) {
    this.id = id;
    this.name = name;
    this.ownerId = ownerId;
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

  public UUID getOwnerId() {
    return ownerId;
  }
}
