package com.bruno.taskflow_api.task.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bruno.taskflow_api.shared.application.port.out.AuthenticatedUserProvider;
import com.bruno.taskflow_api.task.application.exception.TaskListNotFoundException;
import com.bruno.taskflow_api.task.application.port.out.TaskListGateway;
import com.bruno.taskflow_api.task.application.port.out.TaskRepository;
import com.bruno.taskflow_api.task.domain.model.Task;
import com.bruno.taskflow_api.task.domain.model.TaskStatus;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

  @Mock
  private TaskRepository taskRepository;

  @Mock
  private TaskListGateway taskListGateway;

  @Mock
  private AuthenticatedUserProvider authenticatedUserProvider;

  @InjectMocks
  private TaskService taskService;

  @Test
  void shouldCreateTaskSuccessfullyTest() {
    when(taskListGateway.taskListExists(any(UUID.class))).thenReturn(true);
    when(authenticatedUserProvider.getCurrentUserId()).thenReturn(UUID.randomUUID());
    when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Task task = taskService.createTask("Learning Mockito", "Description", UUID.randomUUID());

    assertEquals("Learning Mockito", task.getTitle());
    assertEquals("Description", task.getDescription());
    assertEquals(TaskStatus.TODO, task.getStatus());
    verify(taskRepository).save(any(Task.class));
  }

  @Test
  void shouldThrowWhenTaskListDoesNotExist() {
    UUID taskListId = UUID.randomUUID();

    when(taskListGateway.taskListExists(taskListId)).thenReturn(false);

    assertThrows(TaskListNotFoundException.class,
        () -> taskService.createTask("Título", "desc", taskListId));

    verify(taskRepository, never()).save(any());
  }
}