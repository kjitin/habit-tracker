-- Initial schema for the habit tracker.

CREATE TABLE users (
    id            UUID         PRIMARY KEY,
    email         VARCHAR(254) NOT NULL,
    password_hash VARCHAR(72)  NOT NULL,
    display_name  VARCHAR(100) NOT NULL,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now()
);

-- Case-insensitive uniqueness on email.
CREATE UNIQUE INDEX users_email_lower_uq ON users (LOWER(email));

CREATE TABLE habits (
    id         UUID        PRIMARY KEY,
    owner_id   UUID        NOT NULL REFERENCES users(id),
    name       VARCHAR(200) NOT NULL,
    frequency  VARCHAR(16) NOT NULL,
    deleted    BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Most queries are "habits for owner X that aren't deleted" — partial index.
CREATE INDEX habits_owner_active_idx ON habits (owner_id) WHERE deleted = FALSE;

CREATE TABLE habit_completions (
    id           UUID        PRIMARY KEY,
    habit_id     UUID        NOT NULL REFERENCES habits(id),
    completed_on DATE        NOT NULL,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),

    -- Idempotency: completing the same habit twice on the same day is a no-op.
    CONSTRAINT habit_completions_uq UNIQUE (habit_id, completed_on)
);

CREATE INDEX habit_completions_habit_idx ON habit_completions (habit_id, completed_on DESC);
