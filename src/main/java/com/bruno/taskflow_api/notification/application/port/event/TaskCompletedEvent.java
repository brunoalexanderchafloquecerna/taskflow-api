package com.bruno.taskflow_api.notification.application.port.event;

import java.time.Instant;
import java.util.UUID;

public record TaskCompletedEvent(UUID taskId, UUID ownerId, UUID completedBy, Instant completedAt) {

}
