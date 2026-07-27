package com.bruno.taskflow_api.notification.domain.model;

import java.time.Instant;

public record NotificationEvent(String userId, String eventType, String actorId, Instant timestamp,
                                String payload, Instant expiresAt) {

}
