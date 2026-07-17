package com.bruno.taskflow_api.workspace.application.port.in;

import com.bruno.taskflow_api.workspace.domain.model.Workspace;
import java.util.List;
import java.util.UUID;

public interface WorkspaceUseCase {

  Workspace create(String name, UUID userId);

  Workspace findById(UUID id);

  List<Workspace> findAll();

  Workspace updateName(UUID id, String name);

  void deleteById(UUID id);
}
