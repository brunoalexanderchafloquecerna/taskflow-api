package com.bruno.taskflow_api.user.infrastructure.adapter.out.persistence;

import com.bruno.taskflow_api.user.application.port.out.UserRepository;
import com.bruno.taskflow_api.user.domain.model.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class JpaUserRepositoryAdapter implements UserRepository {

  private final SpringDataUserRepository springDataUserRepository;

  public JpaUserRepositoryAdapter(SpringDataUserRepository springDataUserRepository) {
    this.springDataUserRepository = springDataUserRepository;
  }

  @Override
  public User save(User user) {
    UserJpaEntity userJpaEntity = springDataUserRepository.save(toEntity(user));
    return toDomain(userJpaEntity);
  }

  @Override
  public Optional<User> findById(UUID id) {
    return springDataUserRepository.findById(id).map(this::toDomain);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return springDataUserRepository.findByEmail(email).map(this::toDomain);
  }

  @Override
  public Optional<User> findByKeycloakId(String keycloakId) {
    return springDataUserRepository.findByKeycloakId(keycloakId).map(this::toDomain);
  }

  @Override
  public List<User> findAll() {
    return springDataUserRepository.findAll().stream().map(this::toDomain).toList();
  }

  @Override
  public boolean existsByEmail(String email) {
    return springDataUserRepository.existsByEmail(email);
  }

  @Override
  public boolean existsByEmailAndKeycloakIdNot(String email, String keycloakId) {
    return springDataUserRepository.existsByEmailAndKeycloakIdNot(email, keycloakId);
  }

  @Override
  public void deleteById(UUID id) {
    springDataUserRepository.deleteById(id);
  }

  @Override
  public boolean existsById(UUID id) {
    return springDataUserRepository.existsById(id);
  }

  private User toDomain(UserJpaEntity userJpaEntity) {
    return new User(userJpaEntity.getId(), userJpaEntity.getName(), userJpaEntity.getEmail(),
        userJpaEntity.getKeycloakId());
  }

  private UserJpaEntity toEntity(User user) {
    return new UserJpaEntity(user.getId(), user.getName(), user.getEmail(), user.getKeycloakId());
  }
}
