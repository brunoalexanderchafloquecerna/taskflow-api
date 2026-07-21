package com.bruno.taskflow_api.workspace.application.port.out;

import com.bruno.taskflow_api.workspace.domain.model.Workspace;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspaceRepository {

  Workspace save(Workspace workspace);

  Optional<Workspace> findById(UUID id);

  List<Workspace> findAll();

  void deleteById(UUID id);

  boolean existsByUserId(UUID userId);

  List<Workspace> findAllByUserId(UUID currentUserId);
}
