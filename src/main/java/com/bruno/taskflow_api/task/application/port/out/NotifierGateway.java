package com.bruno.taskflow_api.task.application.port.out;

public interface NotifierGateway {

  void notifyTaskCompleted(String taskId, String ownerId, String actorId);
}
