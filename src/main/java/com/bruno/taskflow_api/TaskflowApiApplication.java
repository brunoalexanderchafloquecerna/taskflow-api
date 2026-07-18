package com.bruno.taskflow_api;

import com.bruno.taskflow_api.task.application.port.out.TaskRepository;
import com.bruno.taskflow_api.task.domain.model.Task;
import com.bruno.taskflow_api.task.domain.model.TaskStatus;
import com.bruno.taskflow_api.tasklist.application.port.out.TaskListRepository;
import com.bruno.taskflow_api.tasklist.domain.model.TaskList;
import com.bruno.taskflow_api.user.application.dto.response.AuthResponse;
import com.bruno.taskflow_api.user.application.port.in.AuthUseCase;
import com.bruno.taskflow_api.user.application.port.in.UserUseCase;
import com.bruno.taskflow_api.user.application.port.out.UserRepository;
import com.bruno.taskflow_api.user.application.service.UserService;
import com.bruno.taskflow_api.user.domain.model.Role;
import com.bruno.taskflow_api.user.domain.model.User;
import com.bruno.taskflow_api.workspace.application.port.out.WorkspaceRepository;
import com.bruno.taskflow_api.workspace.domain.model.Workspace;
import java.time.LocalDateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class TaskflowApiApplication {

  static void main(String[] args) {
    SpringApplication.run(TaskflowApiApplication.class, args);
  }

}

@Component
class SenderInformation implements CommandLineRunner {

  private final AuthUseCase authUseCase;
  private final UserService userService;
  private final WorkspaceRepository workspaceRepository;
  private final TaskListRepository taskListRepository;
  private final TaskRepository taskRepository;

  SenderInformation(AuthUseCase authUseCase, UserService userService, WorkspaceRepository workspaceRepository,
      TaskListRepository taskListRepository, TaskRepository taskRepository) {
    this.authUseCase = authUseCase;
    this.userService = userService;
    this.workspaceRepository = workspaceRepository;
    this.taskListRepository = taskListRepository;
    this.taskRepository = taskRepository;
  }

  @Override
  public void run(String... args) throws Exception {
    AuthResponse register = authUseCase.register("Bruno", Role.ADMIN, "bruno@example.com", "password");
    User user = userService.getUserByEmail("bruno@example.com");
    Workspace workspace = workspaceRepository.save(new Workspace(null, "Work", user.getId()));
    TaskList taskList = taskListRepository.save(
        new TaskList(null, "Certifications", workspace.getId(), 0));
    LocalDateTime now = LocalDateTime.now();
    Task task = taskRepository.save(
        new Task(null, "AZURE", "Study for AZURE certification", TaskStatus.TODO, taskList.getId(),
            user.getId(), now, now));
    Task task2 = taskRepository.save(
        new Task(null, ".NET", "Study for .NET certification", TaskStatus.TODO,
            taskList.getId(), user.getId(), now, now));
  }
}
