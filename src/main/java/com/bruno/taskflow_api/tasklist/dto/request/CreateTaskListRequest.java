package com.bruno.taskflow_api.tasklist.dto.request;

import java.util.UUID;

public record CreateTaskListRequest(String title, String description, UUID taskListId) {

}
