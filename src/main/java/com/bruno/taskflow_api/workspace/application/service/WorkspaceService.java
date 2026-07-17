package com.bruno.taskflow_api.workspace.application.service;

import com.bruno.taskflow_api.shared.application.port.out.AuthenticatedUserProvider;
import com.bruno.taskflow_api.workspace.application.exception.UserNotFoundException;
import com.bruno.taskflow_api.workspace.application.exception.WorkspaceNotFoundException;
import com.bruno.taskflow_api.workspace.application.port.in.WorkspaceUseCase;
import com.bruno.taskflow_api.workspace.application.port.out.UserGateway;
import com.bruno.taskflow_api.workspace.application.port.out.WorkspaceRepository;
import com.bruno.taskflow_api.workspace.domain.model.Workspace;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkspaceService implements WorkspaceUseCase {

  private final WorkspaceRepository workspaceRepository;

  private final AuthenticatedUserProvider authenticatedUserProvider;

  private final UserGateway userGateway;

  public WorkspaceService(WorkspaceRepository workspaceRepository,
      AuthenticatedUserProvider authenticatedUserProvider, UserGateway userGateway) {
    this.workspaceRepository = workspaceRepository;
    this.authenticatedUserProvider = authenticatedUserProvider;
    this.userGateway = userGateway;
  }

  @Override
  @Transactional
  public Workspace create(String name, UUID userId) {
    if (!userGateway.userExistsById(userId)) {
      throw new UserNotFoundException("User with id %s was not found".formatted(userId));
    }
    return workspaceRepository.save(Workspace.create(name, userId));
  }

  @Override
  @Transactional(readOnly = true)
  public Workspace findById(UUID id) {
    return workspaceRepository.findById(id).orElseThrow(
        () -> new WorkspaceNotFoundException("Workspace with id %s was not found".formatted(id)));
  }

  @Override
  @Transactional(readOnly = true)
  public List<Workspace> findAll() {
    return workspaceRepository.findAll();
  }

  @Override
  @Transactional
  public Workspace updateName(UUID id, String name) {
    Workspace workspace = findById(id);
    workspace.updateName(name);
    return workspaceRepository.save(workspace);
  }

  @Override
  @Transactional
  public void deleteById(UUID id) {
    Workspace workspace = findById(id);
    boolean isOwner = workspace.getUserId().equals(authenticatedUserProvider.getCurrentUserId());
    boolean isAdmin = authenticatedUserProvider.currentUserIsAdmin();
    if (!isOwner && !isAdmin) {
      throw new AccessDeniedException(
          "You are not the owner of this workspace and not an administrator.");
    }
    workspaceRepository.deleteById(id);
  }
}
