package com.bruno.taskflow_api.workspace.infrastructure.adapter.out.persistence;

import com.bruno.taskflow_api.workspace.application.port.out.WorkspaceRepository;

public class JpaWorkspaceAdapter implements WorkspaceRepository {

  private final SpringDataWorkspaceRepository springDataWorkspaceRepository;

  public JpaWorkspaceAdapter(SpringDataWorkspaceRepository springDataWorkspaceRepository) {
    this.springDataWorkspaceRepository = springDataWorkspaceRepository;
  }
}
