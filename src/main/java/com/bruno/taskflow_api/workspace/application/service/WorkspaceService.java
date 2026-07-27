package com.bruno.taskflow_api.workspace.application.service;

import com.bruno.taskflow_api.shared.application.port.out.AuthenticatedUserProvider;
import com.bruno.taskflow_api.shared.application.utils.ValidationUtils;
import com.bruno.taskflow_api.workspace.application.exception.WorkspaceNotFoundException;
import com.bruno.taskflow_api.workspace.application.port.in.WorkspaceUseCase;
import com.bruno.taskflow_api.workspace.application.port.out.WorkspaceRepository;
import com.bruno.taskflow_api.workspace.domain.model.Workspace;
import java.util.List;
import java.util.UUID;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkspaceService implements WorkspaceUseCase {

  private final WorkspaceRepository workspaceRepository;

  public WorkspaceService(WorkspaceRepository workspaceRepository) {
    this.workspaceRepository = workspaceRepository;
  }

  @Override
  @Transactional
  public Workspace create(String name, UUID userId) {
    return workspaceRepository.save(Workspace.create(name, userId));
  }

  @Override
  @Cacheable(value = "workspaces", key = "#id")
  @Transactional(readOnly = true)
  public Workspace findById(UUID id) {
    System.out.println(">>> BUSCANDO EN BASE DE DATOS PARA ID: " + id);
    return workspaceRepository.findById(id).orElseThrow(
        () -> new WorkspaceNotFoundException("Workspace with id %s was not found".formatted(id)));
  }

  @Override
  @Transactional(readOnly = true)
  public List<Workspace> findAll() {
    return workspaceRepository.findAll();
  }

  @Override
  @Cacheable(value = "workspaces", key = "'all-for-user' + #currentUserId")
  @Transactional(readOnly = true)
  public List<Workspace> findAllByUserId(UUID currentUserId) {
    System.out.println("<<< BUSCANDO WORKSPACES PARA USER ID: " + currentUserId + " EN LA DB >>>");
    return workspaceRepository.findAllByUserId(currentUserId);
  }

  @Override
  @Transactional
  //@CachePut(value = "workspaces", key = "#id") => Usamos caching para actualiza el cache individual e invalidar el cache por ownerId
  @Caching(put = {@CachePut(value = "workspaces", key = "#id")}, evict = {
      @CacheEvict(value = "workspaces", key = "'all-for-user' + #result.ownerId")})
  public Workspace updateName(UUID id, String name) {
    Workspace workspace = findById(id);
    workspace.updateName(name);
    return workspaceRepository.save(workspace);
  }

  @Override
  public boolean existsByUserId(UUID userId) {
    return workspaceRepository.existsByUserId(userId);
  }

  @Override
  @Transactional
  @CacheEvict(value = "workspaces", key = "#id")
  public void deleteById(UUID id, AuthenticatedUserProvider authenticatedUserProvider) {
    Workspace workspace = findById(id);
    ValidationUtils.isOwnerOrAdmin(authenticatedUserProvider, workspace.getOwnerId());
    workspaceRepository.deleteById(id);
  }
}
