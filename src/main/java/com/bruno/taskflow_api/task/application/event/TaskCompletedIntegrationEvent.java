package com.bruno.taskflow_api.task.application.event;

import java.time.Instant;
import java.util.UUID;

public record TaskCompletedIntegrationEvent(UUID taskId, UUID taskListId, UUID ownerId,
                                            UUID completedBy, Instant completedAt) {

}
