package com.bruno.taskflow_api.user.infrastructure.adapter.in.web;

import com.bruno.taskflow_api.shared.application.port.out.AuthenticatedUserProvider;
import com.bruno.taskflow_api.user.application.port.in.UserUseCase;
import com.bruno.taskflow_api.user.domain.model.User;
import com.bruno.taskflow_api.user.infrastructure.adapter.in.web.dto.request.ChangePasswordRequest;
import com.bruno.taskflow_api.user.infrastructure.adapter.in.web.dto.request.RegisterRequest;
import com.bruno.taskflow_api.user.infrastructure.adapter.in.web.dto.request.UpdateProfileRequest;
import com.bruno.taskflow_api.user.infrastructure.adapter.in.web.dto.response.UserResponse;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserUseCase userUseCase;

  private final AuthenticatedUserProvider authenticatedUserProvider;

  public UserController(UserUseCase userUseCase,
      AuthenticatedUserProvider authenticatedUserProvider) {
    this.userUseCase = userUseCase;
    this.authenticatedUserProvider = authenticatedUserProvider;
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUserById(@Valid @PathVariable UUID id) {
    return ResponseEntity.ok(toResponse(userUseCase.getUserById(id)));
  }

  @GetMapping
  public ResponseEntity<List<UserResponse>> getUsers() {
    return ResponseEntity.ok(userUseCase.getAllUsers().stream().map(this::toResponse).toList());
  }

  @PostMapping("/register")
  public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
    userUseCase.register(request.email(), request.name(), request.password());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping("/me/password")
  public ResponseEntity<Void> changePassword(@RequestHeader("Authorization") String authHeader,
      @Valid @RequestBody ChangePasswordRequest request) {
    userUseCase.changePassword(authHeader, request.currentPassword(), request.newPassword());
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/me/profile")
  public ResponseEntity<Void> updateProfile(@RequestHeader("Authorization") String authHeader,
      @Valid @RequestBody UpdateProfileRequest request) {
    UUID currentUserId = authenticatedUserProvider.getCurrentUserId();
    userUseCase.updateOwnProfile(currentUserId, authHeader, request.email(), request.name());
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{userId}/role/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> promoteToAdmin(@PathVariable UUID userId) {
    userUseCase.promoteToAdmin(userId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/me")
  public ResponseEntity<Void> deleteOwnAccount() {
    UUID currentUserId = authenticatedUserProvider.getCurrentUserId();
    userUseCase.deleteUserById(currentUserId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{userId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteUserAsAdmin(@PathVariable UUID userId) {
    userUseCase.deleteUserById(userId);
    return ResponseEntity.noContent().build();
  }

  private UserResponse toResponse(User user) {
    return new UserResponse(user.getId(), user.getName(), user.getEmail());
  }
}
