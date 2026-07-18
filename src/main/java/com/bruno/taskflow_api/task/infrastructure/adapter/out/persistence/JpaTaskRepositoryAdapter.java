package com.bruno.taskflow_api.task.infrastructure.adapter.out.persistence;

import com.bruno.taskflow_api.task.application.port.out.TaskRepository;
import com.bruno.taskflow_api.task.domain.model.Task;
import com.bruno.taskflow_api.task.domain.model.TaskStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class JpaTaskRepositoryAdapter implements TaskRepository {

  private final SpringDataTaskRepository springDataTaskRepository;

  public JpaTaskRepositoryAdapter(SpringDataTaskRepository springDataTaskRepository) {
    this.springDataTaskRepository = springDataTaskRepository;
  }

  @Override
  public Task save(Task task) {
    TaskJpaEntity taskEntity = toEntity(task);
    TaskJpaEntity taskEntitySaved = springDataTaskRepository.save(taskEntity);
    return toDomain(taskEntitySaved);
  }

  @Override
  public Optional<Task> findById(UUID id) {
    return springDataTaskRepository.findById(id).map(this::toDomain);
  }

  @Override
  public List<Task> findTasksByFilters(UUID currentUserId, UUID taskListId, TaskStatus status) {
    List<TaskJpaEntity> taskEntities;
    if (taskListId != null && status != null) {
      taskEntities = springDataTaskRepository.findByOwnerIdAndStatusAndTaskListId(currentUserId,
          status, taskListId);
    } else if (taskListId != null) {
      taskEntities = springDataTaskRepository.findByOwnerIdAndTaskListId(currentUserId, taskListId);
    } else if (status != null) {
      taskEntities = springDataTaskRepository.findByOwnerIdAndStatus(currentUserId, status);
    } else {
      taskEntities = springDataTaskRepository.findByOwnerId(currentUserId);
    }
    return taskEntities.stream().map(this::toDomain).toList();
  }

  @Override
  public List<Task> findAllTasks() {
    return springDataTaskRepository.findAll().stream().map(this::toDomain).toList();
  }

  @Override
  public void deleteById(UUID id) {
    springDataTaskRepository.deleteById(id);
  }

  @Override
  public void deleteByTaskListId(UUID taskListId) {
    springDataTaskRepository.deleteAllByTaskListId(taskListId);
  }

  private TaskJpaEntity toEntity(Task task) {
    return new TaskJpaEntity(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(),
        task.getTaskListId(), task.getOwnerId(), task.getCreatedAt(), task.getUpdatedAt());
  }

  private Task toDomain(TaskJpaEntity taskJpaEntity) {
    return new Task(taskJpaEntity.getId(), taskJpaEntity.getTitle(), taskJpaEntity.getDescription(),
        taskJpaEntity.getStatus(), taskJpaEntity.getTaskListId(), taskJpaEntity.getOwnerId(),
        taskJpaEntity.getCreatedAt(), taskJpaEntity.getUpdatedAt());
  }
}
