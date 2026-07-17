package com.bruno.taskflow_api.user.infrastructure.adapter.in.web;

import com.bruno.taskflow_api.user.application.dto.response.AuthResponse;
import com.bruno.taskflow_api.user.application.port.in.AuthUseCase;
import com.bruno.taskflow_api.user.infrastructure.adapter.in.web.dto.request.AuthLoginRequest;
import com.bruno.taskflow_api.user.infrastructure.adapter.in.web.dto.request.AuthRegisterRequest;
import com.bruno.taskflow_api.user.infrastructure.adapter.in.web.dto.response.AuthTokenResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthUseCase authUseCase;

  public AuthController(AuthUseCase authUseCase) {
    this.authUseCase = authUseCase;
  }

  @PostMapping("/register")
  public ResponseEntity<AuthTokenResponse> register(
      @RequestBody @Valid AuthRegisterRequest request) {
    AuthResponse authResponse = authUseCase.register(request.name(), request.role(),
        request.email(), request.password());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new AuthTokenResponse(authResponse.token()));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthTokenResponse> login(@RequestBody @Valid AuthLoginRequest request) {
    AuthResponse authResponse = authUseCase.login(request.email(), request.password());
    return ResponseEntity.ok(new AuthTokenResponse(authResponse.token()));
  }
}
