package com.bruno.taskflow_api.user;

import com.bruno.taskflow_api.user.dto.request.CreateUserRequest;
import com.bruno.taskflow_api.user.dto.response.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest createUserRequest) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(createUserRequest));
  }
}
