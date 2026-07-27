package com.bruno.taskflow_api.user.application.service;

import com.bruno.taskflow_api.user.application.port.in.UserSyncUseCase;
import com.bruno.taskflow_api.user.application.port.out.UserRepository;
import com.bruno.taskflow_api.user.domain.model.User;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserSyncService implements UserSyncUseCase {

  private final UserRepository userRepository;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public UserSyncService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  @Transactional
  @Cacheable(value = "user", key = "'for-keycloak-id-' + #keycloakId")
  public User syncFromIdentityProvider(String keycloakId, String email, String name) {
    Optional<User> user = userRepository.findByKeycloakId(keycloakId);
    if (user.isEmpty()) {
      User newUser = User.create(name, email, keycloakId);
      return saveIgnoringConflict(newUser);
    }
    User userExisting = user.get();
    boolean changed = false;
    if (!userExisting.getEmail().equals(email) && !userRepository.existsByEmailAndKeycloakIdNot(
        email, keycloakId)) {
      userExisting.updateEmail(email);
      changed = true;
    }
    if (!userExisting.getName().equals(name)) {
      userExisting.updateName(name);
      changed = true;
    }
    return changed ? userRepository.save(userExisting) : userExisting;
  }

  private User saveIgnoringConflict(User newUser) {
    try {
      return userRepository.save(newUser);
    } catch (DataIntegrityViolationException e) {
      logger.warn("Conflicto de unicidad al sincronizar usuario, reintentando búsqueda: {}",
          e.getMessage());
      return userRepository.findByKeycloakId(newUser.getKeycloakId()).orElseThrow(() -> e);
    }
  }
}
