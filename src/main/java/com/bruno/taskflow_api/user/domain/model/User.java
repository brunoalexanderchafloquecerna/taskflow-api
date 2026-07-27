package com.bruno.taskflow_api.user.domain.model;

import com.bruno.taskflow_api.user.domain.exception.InvalidUserException;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {

  @Serial
  private static final long serialVersionUID = 7641748917251197118L;

  private final UUID id;
  private String name;
  private String email;
  private String keycloakId;

  public User(UUID id, String name, String email, String keycloakId) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.keycloakId = keycloakId;
  }

  public static User create(String name, String email, String keycloakId) {
    if (name == null || name.trim().isEmpty()) {
      throw new InvalidUserException("Name is required");
    }
    if (email == null || email.trim().isEmpty()) {
      throw new InvalidUserException("Email is required");
    }
    if (keycloakId == null || keycloakId.trim().isEmpty()) {
      throw new InvalidUserException("Keycloak ID is required");
    }
    return new User(null, name, email, keycloakId);
  }

  public void updateInformation(String name, String email) {
    if (name != null && !name.trim().isEmpty()) {
      this.name = name;
    }
    if (email != null && !email.trim().isEmpty()) {
      this.email = email;
    }
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getKeycloakId() {
    return keycloakId;
  }

  public void updateEmail(String email) {
    this.email = email;
  }

  public void updateName(String name) {
    this.name = name;
  }
}
