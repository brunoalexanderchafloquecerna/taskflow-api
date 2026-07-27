package com.bruno.taskflow_api.workspace.infrastructure.adapter.out.persistence;

import com.bruno.taskflow_api.workspace.application.port.out.WorkspaceRepository;
import com.bruno.taskflow_api.workspace.domain.model.Workspace;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class JpaWorkspaceAdapter implements WorkspaceRepository {

  private final SpringDataWorkspaceRepository springDataWorkspaceRepository;

  public JpaWorkspaceAdapter(SpringDataWorkspaceRepository springDataWorkspaceRepository) {
    this.springDataWorkspaceRepository = springDataWorkspaceRepository;
  }

  @Override
  public Workspace save(Workspace workspace) {
    WorkspaceJpaEntity workspaceJpaEntity = springDataWorkspaceRepository.save(toEntity(workspace));
    return toDomain(workspaceJpaEntity);
  }

  @Override
  public Optional<Workspace> findById(UUID id) {
    return springDataWorkspaceRepository.findById(id).map(this::toDomain);
  }

  @Override
  public List<Workspace> findAll() {
    return springDataWorkspaceRepository.findAll().stream().map(this::toDomain).toList();
  }

  @Override
  public void deleteById(UUID id) {
    springDataWorkspaceRepository.deleteById(id);
  }

  @Override
  public boolean existsByUserId(UUID userId) {
    return springDataWorkspaceRepository.existsByOwnerId(userId);
  }

  @Override
  public List<Workspace> findAllByUserId(UUID currentUserId) {
    return springDataWorkspaceRepository.findAllByOwnerId(currentUserId).stream().map(this::toDomain)
        .toList();
  }

  private Workspace toDomain(WorkspaceJpaEntity workspaceJpaEntity) {
    return new Workspace(workspaceJpaEntity.getId(), workspaceJpaEntity.getName(),
        workspaceJpaEntity.getOwnerId());
  }

  private WorkspaceJpaEntity toEntity(Workspace workspace) {
    return new WorkspaceJpaEntity(workspace.getId(), workspace.getName(), workspace.getOwnerId());
  }
}
