package com.bruno.taskflow_api.user.infrastructure.adapter.in.web;

import com.bruno.taskflow_api.user.application.port.in.UserUseCase;
import com.bruno.taskflow_api.user.domain.model.User;
import com.bruno.taskflow_api.user.infrastructure.adapter.in.web.dto.request.UpdateUserRequest;
import com.bruno.taskflow_api.user.infrastructure.adapter.in.web.dto.response.UserResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserUseCase userUseCase;

  public UserController(UserUseCase userUseCase) {
    this.userUseCase = userUseCase;
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUserById(@Valid @PathVariable UUID id) {
    return ResponseEntity.ok(toResponse(userUseCase.getUserById(id)));
  }

  @GetMapping
  public ResponseEntity<List<UserResponse>> getUsers() {
    return ResponseEntity.ok(userUseCase.getAllUsers().stream().map(this::toResponse).toList());
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserResponse> updateUser(@Valid @PathVariable UUID id,
      @Valid @RequestBody UpdateUserRequest updateUserRequest) {
    return ResponseEntity.ok(toResponse(
        userUseCase.updateUser(id, updateUserRequest.name(), updateUserRequest.role(),
            updateUserRequest.email(), updateUserRequest.oldPassword(),
            updateUserRequest.newPassword())));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
    userUseCase.deleteUserById(id);
    return ResponseEntity.ok().build();
  }

  private UserResponse toResponse(User user) {
    return new UserResponse(user.getId(), user.getName(), user.getEmail());
  }
}
