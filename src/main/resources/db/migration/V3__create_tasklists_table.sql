CREATE TABLE tasklists
(
    id           UUID PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    workspace_id UUID         NOT NULL,
    owner_id     UUID         NOT NULL,
    position     INTEGER      NOT NULL DEFAULT 0,
    CONSTRAINT fk_tasklists_workspace FOREIGN KEY (workspace_id) REFERENCES workspaces (id),
    CONSTRAINT fk_tasklists_owner FOREIGN KEY (owner_id) REFERENCES users (id)
);

CREATE INDEX idx_tasklists_workspace_id ON tasklists (workspace_id);
CREATE INDEX idx_tasklists_owner_id ON tasklists (owner_id);