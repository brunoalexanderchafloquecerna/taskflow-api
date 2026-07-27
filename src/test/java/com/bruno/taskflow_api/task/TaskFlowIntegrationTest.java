package com.bruno.taskflow_api.task;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bruno.taskflow_api.BaseIntegrationTest;
import com.bruno.taskflow_api.tasklist.application.port.out.TaskListRepository;
import com.bruno.taskflow_api.tasklist.domain.model.TaskList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class TaskFlowIntegrationTest extends BaseIntegrationTest {

  @Autowired
  private TaskListRepository taskListRepositoryAdapter;

  private UUID existingTaskListId;

  @BeforeEach
  void setUp() {
    TaskList taskList = TaskList.create("Lista de prueba", UUID.randomUUID(), UUID.randomUUID(), 0);
    existingTaskListId = taskListRepositoryAdapter.save(taskList).getId();
  }

  @Test
  void shouldCreateAndRetrieveTaskEndToEnd() throws Exception {
    String keycloakId = UUID.randomUUID().toString();

    String requestBody = """
        {"title": "Prueba end-to-end", "description": "desc", "taskListId": "%s"}
        """.formatted(existingTaskListId);

    mockMvc.perform(post("/api/tasks").with(jwt().jwt(
                    builder -> builder.claim("sub", keycloakId).claim("email", "test@taskflow.com")
                        .claim("name", "Test User").claim("realm_access", Map.of("roles", List.of("USER"))))
                .authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isCreated()).andExpect(jsonPath("$.title").value("Prueba end-to-end"));
  }
}
