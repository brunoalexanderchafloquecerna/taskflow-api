package com.bruno.taskflow_api.tasklist.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tasklists")
public class TaskListJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;

  @Column(name = "workspace_id", nullable = false)
  private UUID workspaceId;

  private int position;

  protected TaskListJpaEntity() {
  }

  public TaskListJpaEntity(UUID id, String name, UUID workspaceId, int position) {
    this.id = id;
    this.name = name;
    this.workspaceId = workspaceId;
    this.position = position;
  }
}
