package com.bruno.taskflow_api.user.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

  @Column(nullable = false, unique = true)
  private String email;

  private String name;

  private String password;

  protected UserJpaEntity() {
  }

  public UserJpaEntity(UUID id, String email, String name, String password) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.password = password;
  }
}
