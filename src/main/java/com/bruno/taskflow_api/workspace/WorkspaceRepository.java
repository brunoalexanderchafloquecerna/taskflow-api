package com.bruno.taskflow_api.workspace;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {

  @Query("SELECT w FROM Workspace w LEFT JOIN FETCH w.taskLists WHERE w.id = :id")
  Optional<Workspace> findByIdWithTaskLists(@Param("id") UUID id);
}
