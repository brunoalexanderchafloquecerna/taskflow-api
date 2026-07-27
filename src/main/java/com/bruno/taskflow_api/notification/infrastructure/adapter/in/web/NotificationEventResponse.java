package com.bruno.taskflow_api.notification.infrastructure.adapter.in.web;

import java.time.Instant;

public record NotificationEventResponse(String userId, String eventType, String actorId,
                                        Instant timestamp, String payload, Instant expiresAt) {

}
