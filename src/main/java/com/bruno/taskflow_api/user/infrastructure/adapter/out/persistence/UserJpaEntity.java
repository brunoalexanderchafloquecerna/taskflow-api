package com.bruno.taskflow_api.user.infrastructure.adapter.out.persistence;

import com.bruno.taskflow_api.user.domain.model.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "users")
public class UserJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Column(nullable = false, unique = true)
  private String email;

  private String password;

  protected UserJpaEntity() {
  }

  public UserJpaEntity(UUID id, String name, Role role, String email, String password) {
    this.id = id;
    this.name = name;
    this.role = role;
    this.email = email;
    this.password = password;
  }
}
