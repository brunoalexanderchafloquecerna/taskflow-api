package com.bruno.taskflow_api.task.domain.model;

import java.time.Instant;

public record ActivityEvent(String taskId, String eventType, String actorId, Instant timestamp,
                            String payload) {

}
