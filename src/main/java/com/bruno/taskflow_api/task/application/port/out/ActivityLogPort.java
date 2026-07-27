package com.bruno.taskflow_api.task.application.port.out;

import com.bruno.taskflow_api.task.domain.model.ActivityEvent;
import java.util.List;

public interface ActivityLogPort {

  void record(ActivityEvent event);

  List<ActivityEvent> findByTaskId(String taskId);
}
