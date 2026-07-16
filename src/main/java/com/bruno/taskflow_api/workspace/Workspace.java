package com.bruno.taskflow_api.workspace;

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
@Table(name = "workspaces")
public class Workspace {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  protected Workspace() {
  }

  public Workspace(UUID id, String name, UUID userId) {
    this.id = id;
    this.name = name;
    this.userId = userId;
  }
}
