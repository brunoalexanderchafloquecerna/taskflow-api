package com.bruno.taskflow_api.workspace;

import com.bruno.taskflow_api.user.User;
import java.util.Objects;
import java.util.UUID;

public class Workspace {

  private UUID id;

  private String name;

  private User owner;

  public Workspace(UUID id, String name, User owner) {
    this.id = id;
    this.name = name;
    this.owner = owner;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Workspace workspace)) return false;
    return Objects.equals(id, workspace.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Workspace{id=%s, name=%s, owner=%s}".formatted(id, name, owner);
  }
}
