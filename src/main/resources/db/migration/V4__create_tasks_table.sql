CREATE TABLE tasks
(
    id           UUID PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    description  VARCHAR(2000),
    status       VARCHAR(20),
    task_list_id UUID         NOT NULL,
    owner_id     UUID         NOT NULL,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tasks_task_list FOREIGN KEY (task_list_id) REFERENCES tasklists (id),
    CONSTRAINT fk_tasks_owner FOREIGN KEY (owner_id) REFERENCES users (id)
);
CREATE INDEX idx_tasks_task_list_id ON tasks (task_list_id);
CREATE INDEX idx_tasks_owner_id ON tasks (owner_id);
CREATE INDEX idx_tasks_status ON tasks (status);