package com.bruno.taskflow_api.tasklist.infrastructure.adapter.out.persistence;

import com.bruno.taskflow_api.tasklist.application.port.out.TaskListRepository;
import com.bruno.taskflow_api.tasklist.domain.model.TaskList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class JpaTaskListRepositoryAdapter implements TaskListRepository {

  private final SpringDataTaskListRepository springDataTaskListRepository;

  public JpaTaskListRepositoryAdapter(SpringDataTaskListRepository springDataTaskListRepository) {
    this.springDataTaskListRepository = springDataTaskListRepository;
  }

  @Override
  public TaskList save(TaskList taskList) {
    TaskListJpaEntity taskListJpaEntity = springDataTaskListRepository.save(toEntity(taskList));
    return toDomain(taskListJpaEntity);
  }

  @Override
  public Optional<TaskList> findById(UUID taskListId) {
    return springDataTaskListRepository.findById(taskListId).map(this::toDomain);
  }

  @Override
  public List<TaskList> findByFilters(UUID workspaceId) {
    List<TaskListJpaEntity> taskLists;
    if (workspaceId != null) {
      taskLists = springDataTaskListRepository.findByWorkspaceId(workspaceId);
    } else {
      taskLists = springDataTaskListRepository.findAll();
    }
    return taskLists.stream().map(this::toDomain).toList();
  }

  @Override
  public boolean existsById(UUID taskListId) {
    return springDataTaskListRepository.existsById(taskListId);
  }

  @Override
  public void delete(UUID id) {
    springDataTaskListRepository.deleteById(id);
  }

  private TaskListJpaEntity toEntity(TaskList taskList) {
    return new TaskListJpaEntity(taskList.getId(), taskList.getName(), taskList.getWorkspaceId(),
        taskList.getPosition());
  }

  private TaskList toDomain(TaskListJpaEntity taskListJpaEntity) {
    return new TaskList(taskListJpaEntity.getId(), taskListJpaEntity.getName(),
        taskListJpaEntity.getWorkspaceId(), taskListJpaEntity.getPosition());
  }
}
