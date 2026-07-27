package com.bruno.taskflow_api.task.infrastructure.adapter.out.persistence.dynamodb;

import java.time.Instant;

final class ActivityLogKeys {

  private static final String PK_PREFIX = "TASK#";
  private static final String SK_PREFIX = "ACT#";

  private ActivityLogKeys() {
  }

  static String buildPk(String taskId) {
    return PK_PREFIX + taskId;
  }

  static String buildSk(Instant timestamp) {
    return SK_PREFIX + timestamp.toString();
  }

  static String extractTaskId(String pk) {
    return pk.substring(PK_PREFIX.length());
  }
}