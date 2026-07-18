package com.bruno.taskflow_api.shared.application.utils;

import com.bruno.taskflow_api.shared.application.port.out.AuthenticatedUserProvider;
import java.util.UUID;
import org.springframework.security.access.AccessDeniedException;

public class ValidationUtils {

  public static void isOwnerOrAdmin(AuthenticatedUserProvider authenticatedUserProvider,
      UUID resourceOwnerId) {
    boolean isOwner = resourceOwnerId.equals(authenticatedUserProvider.getCurrentUserId());
    boolean isAdmin = authenticatedUserProvider.currentUserIsAdmin();
    if (!isOwner && !isAdmin) {
      throw new AccessDeniedException("You are not allowed to change task status");
    }
  }
}
