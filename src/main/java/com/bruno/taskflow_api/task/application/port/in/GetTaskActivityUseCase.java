package com.bruno.taskflow_api.task.application.port.in;

import com.bruno.taskflow_api.task.domain.model.ActivityEvent;
import java.util.List;
import java.util.UUID;

public interface GetTaskActivityUseCase {

  List<ActivityEvent> findAllEventsByTaskId(UUID taskId);
}
