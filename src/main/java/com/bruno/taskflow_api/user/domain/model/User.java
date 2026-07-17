package com.bruno.taskflow_api.user.domain.model;

import com.bruno.taskflow_api.user.domain.exception.InvalidUserException;
import java.util.UUID;

public class User {

  private final UUID id;
  private String name;
  private Role role;
  private String email;
  private String password;

  public User(UUID id, String name, Role role, String email, String password) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.role = role;
    this.password = password;
  }

  public static User create(String name, Role role, String email, String password) {
    if (name == null || name.trim().isEmpty()) {
      throw new InvalidUserException("Name is required");
    }
    if (role == null) {
      throw new InvalidUserException("Role is required");
    }
    if (email == null || email.trim().isEmpty()) {
      throw new InvalidUserException("Email is required");
    }
    if (password == null || password.trim().isEmpty()) {
      throw new InvalidUserException("Password is required");
    }
    return new User(null, name, role, email, password);
  }

  public void updateInformation(String name, Role role, String email, String password) {
    if (name != null && !name.trim().isEmpty()) {
      this.name = name;
    }
    if (role != null) {
      this.role = role;
    }
    if (email != null && !email.trim().isEmpty()) {
      this.email = email;
    }
    if (password != null && !password.trim().isEmpty()) {
      this.password = password;
    }
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Role getRole() {
    return role;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }
}
