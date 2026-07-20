package com.bruno.taskflow_api.shared.infrastructure.adapter.out.security;

import com.bruno.taskflow_api.shared.application.port.out.AuthenticatedUserProvider;
import com.bruno.taskflow_api.user.application.port.in.UserSyncUseCase;
import java.util.Objects;
import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityAuthenticatedUserProvider implements AuthenticatedUserProvider {

  private final UserSyncUseCase userSyncUseCase;

  public SpringSecurityAuthenticatedUserProvider(UserSyncUseCase userSyncUseCase) {
    this.userSyncUseCase = userSyncUseCase;
  }

  @Override
  public boolean currentUserIsAdmin() {
    return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
        .anyMatch(authority -> Objects.equals(authority.getAuthority(), "ROLE_ADMIN"));
  }

  @Override
  public UUID getCurrentUserId() {
    Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String keycloakId = jwt.getSubject();
    String email = jwt.getClaim("email");
    String name = jwt.getClaim("name");
    return userSyncUseCase.syncFromIdentityProvider(keycloakId, email, name).getId();
  }
}
