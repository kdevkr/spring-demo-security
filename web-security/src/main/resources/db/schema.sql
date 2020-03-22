-- User
CREATE TABLE IF NOT EXISTS users
(
    user_id  BIGSERIAL NOT NULL,
    id       VARCHAR   NOT NULL,
    password VARCHAR   NOT NULL,
    name     VARCHAR   NOT NULL,
    role     VARCHAR   NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (user_id),
    CONSTRAINT users_uq UNIQUE (id)
);

-- authority of User
CREATE TABLE IF NOT EXISTS users_authority
(
    user_id   BIGINT  NOT NULL,
    authority VARCHAR NOT NULL,
    CONSTRAINT users_authority_pk PRIMARY KEY (user_id, authority),
    CONSTRAINT users_authority_user_id_fk FOREIGN KEY (user_id) REFERENCES users (user_id)
);

-- Repository
CREATE TABLE IF NOT EXISTS repository
(
    id          BIGSERIAL NOT NULL,
    name        VARCHAR   NOT NULL,
    description VARCHAR,
    is_public   BOOLEAN   NOT NULL DEFAULT FALSE,
    created     TIMESTAMP NOT NULL DEFAULT (now() at time zone 'utc'),
    modified    TIMESTAMP NOT NULL DEFAULT (now() at time zone 'utc'),
    owner_id    BIGINT    NOT NULL,
    CONSTRAINT repository_pk PRIMARY KEY (id),
    CONSTRAINT repository_owner_id_fk FOREIGN KEY (owner_id) REFERENCES users (user_id)
);

