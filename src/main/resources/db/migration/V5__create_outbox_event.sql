CREATE TABLE outbox_event
(
    id             UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    aggregate_type VARCHAR(50)  NOT NULL, -- 'TASK', más adelante 'WORKSPACE', etc.
    aggregate_id   UUID         NOT NULL, -- id de la Task (o el agregado que sea)
    event_type     VARCHAR(100) NOT NULL, -- 'TASK_COMPLETED'
    payload        JSONB        NOT NULL, -- evento serializado
    created_at     TIMESTAMPTZ  NOT NULL DEFAULT now(),
    processed_at   TIMESTAMPTZ NULL       -- NULL = pendiente de publicar
);

-- Index parcial: la query de polling SOLO busca processed_at IS NULL,
-- indexar la tabla completa por esa columna sería desperdiciar espacio
-- una vez que la mayoría de filas ya estén procesadas.
CREATE INDEX idx_outbox_pending ON outbox_event (created_at) WHERE processed_at IS NULL;