package com.bruno.taskflow_api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskflowApiApplication {

  static void main(String[] args) {
    SpringApplication.run(TaskflowApiApplication.class, args);
  }

}

/*@Component
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
}*/
