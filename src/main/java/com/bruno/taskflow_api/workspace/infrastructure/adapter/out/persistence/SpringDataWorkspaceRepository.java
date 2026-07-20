package com.bruno.taskflow_api.workspace.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataWorkspaceRepository extends JpaRepository<WorkspaceJpaEntity, UUID> {

  boolean existsByUserId(UUID userId);

  List<WorkspaceJpaEntity> findAllByUserId(UUID userId);
/*  @Query("SELECT w FROM Workspace w LEFT JOIN FETCH w.taskLists WHERE w.id = :id")
  Optional<Workspace> findByIdWithTaskLists(@Param("id") UUID id);*/
}
