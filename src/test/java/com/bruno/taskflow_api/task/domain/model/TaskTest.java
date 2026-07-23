package com.bruno.taskflow_api.task.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.bruno.taskflow_api.task.domain.exception.InvalidTaskException;
import com.bruno.taskflow_api.task.domain.exception.InvalidTaskTransitionException;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class TaskTest {

  private final UUID taskListId = UUID.randomUUID();

  private final UUID ownerId = UUID.randomUUID();

  @Nested
  @DisplayName("Al actualizar la información de una tarea")
  class UpdateInformation {

    @Test
    @DisplayName("Debe actualizar la información de la tarea")
    void shouldUpdateInformation() {
      Task task = Task.create("title", "description", taskListId, ownerId);

      task.updateInformation("newTitle", "newDescription", TaskStatus.IN_PROGRESS, taskListId);

      assertEquals("newTitle", task.getTitle());
      assertEquals("newDescription", task.getDescription());
      assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
    }
  }

  @Nested
  @DisplayName("Al crear una tarea con create()")
  class CreateTask {

    @Test
    @DisplayName("Debe iniciar en estado TODO")
    void shouldStartInTodoStatus() {
      Task task = Task.create("title", "description", taskListId, ownerId);

      assertEquals(TaskStatus.TODO, task.getStatus());
    }

    @Test
    @DisplayName("deberia rechazar titulo vacio")
    void shouldRejectBlakTitle() {
      assertThrows(InvalidTaskException.class,
          () -> Task.create("", "description", taskListId, ownerId));
    }

    @Test
    @DisplayName("deberia rechazar titulo nulo")
    void shouldRejectNullTitle() {
      assertThrows(InvalidTaskException.class,
          () -> Task.create(null, "description", taskListId, ownerId));
    }
  }

  @Nested
  @DisplayName("Al cambiar un estado con moveTo()")
  class MoveTo {

    @Test
    @DisplayName("Deberia permitir TODO -> IN_PROGRESS")
    void shouldAllowFromTodoToInProgress() {
      Task task = Task.create("title", "description", taskListId, ownerId);

      task.moveTo(TaskStatus.IN_PROGRESS);

      assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
    }

    @Test
    @DisplayName("Deberia permitir IN_PROGRESS -> DONE")
    void shouldAllowFromInProgressToDone() {
      Task task = Task.create("title", "description", taskListId, ownerId);
      task.moveTo(TaskStatus.IN_PROGRESS);

      task.moveTo(TaskStatus.DONE);

      assertEquals(TaskStatus.DONE, task.getStatus());
    }

    @Test
    @DisplayName("Deberia rechazar DONE -> TODO directo")
    void shouldRejectFromDoneToTodoDirectly() {
      Task task = Task.create("title", "description", taskListId, ownerId);
      task.moveTo(TaskStatus.IN_PROGRESS);
      task.moveTo(TaskStatus.DONE);

      assertThrows(InvalidTaskTransitionException.class, () -> task.moveTo(TaskStatus.TODO));
    }

    @Test
    @DisplayName("Deberia actualizar updateAt al cambiar de estado")
    void shouldUpdateAtOnStatusChange() {
      LocalDateTime initial = LocalDateTime.of(2026, 1, 1, 10, 0);
      LocalDateTime later = LocalDateTime.of(2026, 1, 1, 10, 5);

      try (MockedStatic<LocalDateTime> mocked = Mockito.mockStatic(LocalDateTime.class)) {
        mocked.when(LocalDateTime::now).thenReturn(initial);
        Task task = Task.create("title", "description", taskListId, ownerId);

        mocked.when(LocalDateTime::now).thenReturn(later);
        task.moveTo(TaskStatus.IN_PROGRESS);

        assertEquals(later, task.getUpdatedAt());
      }
    }
  }
}