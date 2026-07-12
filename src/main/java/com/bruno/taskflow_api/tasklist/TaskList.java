package com.bruno.taskflow_api.tasklist;

import com.bruno.taskflow_api.workspace.Workspace;
import java.util.Objects;
import java.util.UUID;

public class TaskList {

  private UUID id;

  private String name;

  private Workspace workspace;

  private int position;

  public TaskList(UUID id, String name, Workspace workspace, int position) {
    this.id = id;
    this.name = name;
    this.workspace = workspace;
    this.position = position;
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

  public Workspace getWorkspace() {
    return workspace;
  }

  public void setWorkspace(Workspace workspace) {
    this.workspace = workspace;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof TaskList taskList)) return false;
    return Objects.equals(id, taskList.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "TaskList{id=%s, name=%s, workspace=%s, position=%d}".formatted(id, name, workspace,
        position);
  }
}
