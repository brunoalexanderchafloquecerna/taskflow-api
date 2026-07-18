package com.bruno.taskflow_api.shared.infrastructure.adapter.out.security;

import com.bruno.taskflow_api.shared.application.port.out.AuthenticatedUserProvider;
import com.bruno.taskflow_api.user.infrastructure.adapter.out.security.CustomUserDetails;
import java.util.Objects;
import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityAuthenticatedUserProvider implements AuthenticatedUserProvider {

  @Override
  public boolean currentUserIsAdmin() {
    return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
        .getAuthorities().stream()
        .anyMatch(authority -> Objects.equals(authority.getAuthority(), "ROLE_ADMIN"));
  }

  @Override
  public UUID getCurrentUserId() {
    if (Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal() instanceof CustomUserDetails customUserDetails) {
      return customUserDetails.getId();
    }
    throw new IllegalStateException("Current user is not authenticated or does not have a valid ID");
  }
}
