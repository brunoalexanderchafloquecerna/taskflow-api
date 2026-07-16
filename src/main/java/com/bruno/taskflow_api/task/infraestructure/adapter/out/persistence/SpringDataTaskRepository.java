package com.bruno.taskflow_api.task.infraestructure.adapter.out.persistence;

import com.bruno.taskflow_api.task.domain.model.TaskStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataTaskRepository extends JpaRepository<TaskJpaEntity, UUID> {

  List<TaskJpaEntity> findByStatus(TaskStatus status);

  List<TaskJpaEntity> findByTaskListId(UUID taskListId);

  List<TaskJpaEntity> findByStatusAndTaskListId(TaskStatus status, UUID taskListId);

  /*Funciona pero Hibernate primero hace consulta SELECT byTaskListId y elimina uno por uno
  void deleteAllByTaskListId(UUID taskListId);*/

  @Modifying
  @Query("DELETE FROM TaskJpaEntity t WHERE t.taskListId = :taskListId")
  void deleteAllByTaskListId(@Param("taskListId") UUID taskListId);
}
