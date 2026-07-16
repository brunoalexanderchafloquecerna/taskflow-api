package com.bruno.taskflow_api.user.infrastructure.adapter.out.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUserRepository extends JpaRepository<UserJpaEntity, UUID> {

  boolean existsByEmail(String email);
}
