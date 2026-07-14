package com.bruno.taskflow_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskflowApiApplication {

  static void main(String[] args) {
    SpringApplication.run(TaskflowApiApplication.class, args);
  }

}
/*
@Component
class SenderInformation implements CommandLineRunner {

  private final UserRepository userRepository;
  private final WorkspaceRepository workspaceRepository;
  private final TaskListRepository taskListRepository;
  private final TaskRepository taskRepository;

  SenderInformation(UserRepository userRepository, WorkspaceRepository workspaceRepository,
      TaskListRepository taskListRepository, TaskRepository taskRepository) {
    this.userRepository = userRepository;
    this.workspaceRepository = workspaceRepository;
    this.taskListRepository = taskListRepository;
    this.taskRepository = taskRepository;
  }

  @Override
  public void run(String... args) throws Exception {
    User user = userRepository.save(new User(null, "john.doe@example.com", "John Doe", "password"));
    Workspace workspace = workspaceRepository.save(new Workspace(null, "Work", user));
    TaskList taskList = taskListRepository.save(new TaskList(null, "Certifications", workspace, 0));
    LocalDateTime now = LocalDateTime.now();
    Task task = taskRepository.save(
        new Task(null, "AWS", "Study for AWS certification", TaskStatusEnum.TODO, taskList, now,
            now));
    Task task2 = taskRepository.save(
        new Task(null, "SpringBoot", "Study for SpringBoot certification", TaskStatusEnum.TODO,
            taskList, now, now));
  }
}*/
