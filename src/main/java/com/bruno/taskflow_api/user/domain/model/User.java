package com.bruno.taskflow_api.user.domain.model;

import com.bruno.taskflow_api.user.domain.exception.InvalidUserException;
import java.util.UUID;

public class User {

  private final UUID id;
  private String email;
  private String name;
  private String password;

  public User(UUID id, String email, String name, String password) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.password = password;
  }

  public static User create(String email, String name, String password) {
    if (email == null || email.trim().isEmpty()) {
      throw new InvalidUserException("Email is required");
    }
    if (name == null || name.trim().isEmpty()) {
      throw new InvalidUserException("Name is required");
    }
    if (password == null || password.trim().isEmpty()) {
      throw new InvalidUserException("Password is required");
    }
    return new User(null, email, name, password);
  }

  public void updateInformation(String email, String name, String password) {
    if (email != null && !email.trim().isEmpty()) {
      this.email = email;
    }
    if (name != null && !name.trim().isEmpty()) {
      this.name = name;
    }
    if (password != null && !password.trim().isEmpty()) {
      this.password = password;
    }
  }

  public UUID getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }

  public String getPassword() {
    return password;
  }
}
