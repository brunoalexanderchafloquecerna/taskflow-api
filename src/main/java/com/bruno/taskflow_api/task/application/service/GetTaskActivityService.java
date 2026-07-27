package com.bruno.taskflow_api.task.application.service;

import com.bruno.taskflow_api.task.application.port.in.GetTaskActivityUseCase;
import com.bruno.taskflow_api.task.application.port.out.ActivityLogPort;
import com.bruno.taskflow_api.task.domain.model.ActivityEvent;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class GetTaskActivityService implements GetTaskActivityUseCase {

  private final ActivityLogPort activityLogPort;

  public GetTaskActivityService(ActivityLogPort activityLogPort) {
    this.activityLogPort = activityLogPort;
  }

  @Override
  public List<ActivityEvent> findAllEventsByTaskId(UUID taskId) {
    return activityLogPort.findByTaskId(taskId.toString());
  }
}
