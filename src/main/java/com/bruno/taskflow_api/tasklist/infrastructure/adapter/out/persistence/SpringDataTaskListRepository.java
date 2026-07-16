package com.bruno.taskflow_api.tasklist.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataTaskListRepository extends JpaRepository<TaskListJpaEntity, UUID> {

  List<TaskListJpaEntity> findByWorkspaceId(UUID workspaceId);
}
