package com.bruno.taskflow_api.shared.application.port.out;

import java.util.UUID;

public interface AuthenticatedUserProvider {

  boolean currentUserIsAdmin();

  UUID getCurrentUserId();
}
