package com.bruno.taskflow_api.task.infrastructure.adapter.out.persistence;

import com.bruno.taskflow_api.task.domain.model.TaskStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tasks")
public class TaskJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String title;

  private String description;

  @Enumerated(EnumType.STRING)
  private TaskStatus status;

  @Column(name = "task_list_id", nullable = false)
  private UUID taskListId;

  @Column(name = "owner_id", nullable = false)
  private UUID ownerId;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  protected TaskJpaEntity() {
  }

  public TaskJpaEntity(UUID id, String title, String description, TaskStatus status,
      UUID taskListId, UUID ownerId, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.status = status;
    this.taskListId = taskListId;
    this.ownerId = ownerId;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
}
