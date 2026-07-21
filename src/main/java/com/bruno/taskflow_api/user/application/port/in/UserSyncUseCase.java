package com.bruno.taskflow_api.user.application.port.in;

import com.bruno.taskflow_api.user.domain.model.User;

public interface UserSyncUseCase {

  User syncFromIdentityProvider(String keycloakId, String email, String name);
}
