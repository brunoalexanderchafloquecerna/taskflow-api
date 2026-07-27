package com.bruno.taskflow_api.workspace.infrastructure.adapter.in.web;

import com.bruno.taskflow_api.shared.application.port.out.AuthenticatedUserProvider;
import com.bruno.taskflow_api.workspace.application.port.in.WorkspaceUseCase;
import com.bruno.taskflow_api.workspace.domain.model.Workspace;
import com.bruno.taskflow_api.workspace.infrastructure.adapter.in.web.dto.request.CreateWorkspaceRequest;
import com.bruno.taskflow_api.workspace.infrastructure.adapter.in.web.dto.request.UpdateWorkspaceRequest;
import com.bruno.taskflow_api.workspace.infrastructure.adapter.in.web.dto.response.WorkspaceResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceController {

  private final WorkspaceUseCase workspaceUseCase;

  private final AuthenticatedUserProvider authenticatedUserProvider;

  public WorkspaceController(WorkspaceUseCase workspaceUseCase,
      AuthenticatedUserProvider authenticatedUserProvider) {
    this.workspaceUseCase = workspaceUseCase;
    this.authenticatedUserProvider = authenticatedUserProvider;
  }

  @PostMapping()
  public ResponseEntity<WorkspaceResponse> createWorkspace(
      @Valid @RequestBody CreateWorkspaceRequest createWorkspaceRequest) {
    Workspace workspace = workspaceUseCase.create(createWorkspaceRequest.name(),
        authenticatedUserProvider.getCurrentUserId());
    return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(workspace));
  }

  @GetMapping("/all")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<WorkspaceResponse>> findAllWorkspaces() {
    return ResponseEntity.ok(workspaceUseCase.findAll().stream().map(this::toResponse).toList());
  }

  @GetMapping()
  public ResponseEntity<List<WorkspaceResponse>> findAllWorkspacesByUser() {
    UUID currentUserId = authenticatedUserProvider.getCurrentUserId();
    return ResponseEntity.ok(
        workspaceUseCase.findAllByUserId(currentUserId).stream().map(this::toResponse).toList());
  }

  @GetMapping("/{id}")
  public ResponseEntity<WorkspaceResponse> findWorkspaceById(@PathVariable UUID id) {
    Workspace workspace = workspaceUseCase.findById(id);
    return ResponseEntity.ok(toResponse(workspace));
  }

  @PutMapping("/{id}")
  public ResponseEntity<WorkspaceResponse> updateWorkspace(@PathVariable UUID id,
      @Valid @RequestBody UpdateWorkspaceRequest updateWorkspaceRequest) {
    Workspace workspace = workspaceUseCase.updateName(id, updateWorkspaceRequest.name());
    return ResponseEntity.ok().body(toResponse(workspace));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteWorkspaceById(@PathVariable UUID id) {
    workspaceUseCase.deleteById(id, authenticatedUserProvider);
    return ResponseEntity.noContent().build();
  }

  private WorkspaceResponse toResponse(Workspace workspace) {
    return new WorkspaceResponse(workspace.getId(), workspace.getName(), workspace.getOwnerId());
  }
}
