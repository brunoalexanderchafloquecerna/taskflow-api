package com.bruno.taskflow_api.notification.infrastructure.adapter.persistence;

import java.time.Instant;

final class NotificationKeys {

  private static final String PK_PREFIX = "USER#";
  private static final String SK_PREFIX = "NOTIF#";

  private NotificationKeys() {
  }

  static String buildPk(String userId) {
    return PK_PREFIX + userId;
  }

  static String buildSk(Instant timestamp) {
    return SK_PREFIX + timestamp.toString();
  }

  static String extractUserId(String pk) {
    return pk.substring(PK_PREFIX.length());
  }
}
