package com.bruno.taskflow_api.task.infrastructure.adapter.in.web.dto.response;

import java.time.Instant;

public record ActivityEventResponse(String taskId, String eventType, Instant timestamp,
                                    String payload) {

}
