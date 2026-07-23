package com.bruno.taskflow_api.task.infrastructure.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.bruno.taskflow_api.task.domain.model.Task;
import com.bruno.taskflow_api.task.domain.model.TaskStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Tag("integration")
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaTaskRepositoryAdapter.class)
class JpaTaskRepositoryAdapterTest {

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
      "postgres:16-alpine");

  @Autowired
  private JpaTaskRepositoryAdapter jpaTaskRepositoryAdapter;

  @Test
  void shouldSaveAndRetrieveTaskById() {
    UUID taskListId = UUID.randomUUID();
    UUID ownerId = UUID.randomUUID();
    Task task = Task.create("Aprender Testcontainers", "desc", taskListId, ownerId);

    Task saved = jpaTaskRepositoryAdapter.save(task);
    Optional<Task> found = jpaTaskRepositoryAdapter.findById(saved.getId());

    assertThat(found).isPresent();
    assertThat(found.get().getTitle()).isEqualTo("Aprender Testcontainers");
    assertThat(found.get().getStatus()).isEqualTo(TaskStatus.TODO);
  }

  @Test
  void shouldFilterTasksByTaskListIdAndStatus() {
    UUID taskListId = UUID.randomUUID();
    UUID ownerId = UUID.randomUUID();
    Task task1 = Task.create("Tarea A", "desc", taskListId, ownerId);
    Task task2 = Task.create("Tarea B", "desc", taskListId, ownerId);
    jpaTaskRepositoryAdapter.save(task1);
    Task saved2 = jpaTaskRepositoryAdapter.save(task2);
    saved2.moveTo(TaskStatus.DONE);
    jpaTaskRepositoryAdapter.save(saved2);

    List<Task> todoTasks = jpaTaskRepositoryAdapter.findTasksByFilters(ownerId, taskListId,
        TaskStatus.TODO);

    assertThat(todoTasks).hasSize(1);
    assertThat(todoTasks.getFirst().getTitle()).isEqualTo("Tarea A");
  }
}