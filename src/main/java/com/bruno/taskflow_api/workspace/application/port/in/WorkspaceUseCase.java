package com.bruno.taskflow_api.workspace.application.port.in;

import com.bruno.taskflow_api.workspace.domain.model.Workspace;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspaceUseCase {

  Workspace create(String name, UUID userId);

  Workspace findById(UUID id);

  List<Workspace> findAll();

  List<Workspace> findAllByUserId(UUID currentUserId);

  Workspace updateName(UUID id, String name);

  boolean existsByUserId(UUID userId);

  void deleteById(UUID id);
}
