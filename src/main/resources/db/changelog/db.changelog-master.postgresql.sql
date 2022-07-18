--liquibase formatted sql

--changeset manoel.amaro:1
CREATE TABLE IF NOT EXISTS event_journal(
    ordering BIGSERIAL,
    persistence_id VARCHAR(255) NOT NULL,
    sequence_number BIGINT NOT NULL,
    deleted BOOLEAN DEFAULT FALSE NOT NULL,

    writer VARCHAR(255) NOT NULL,
    write_timestamp BIGINT,
    adapter_manifest VARCHAR(255),

    event_ser_id INTEGER NOT NULL,
    event_ser_manifest VARCHAR(255) NOT NULL,
    event_payload BYTEA NOT NULL,

    meta_ser_id INTEGER,
    meta_ser_manifest VARCHAR(255),
    meta_payload BYTEA,

    PRIMARY KEY(persistence_id, sequence_number)
);
-- rollback DROP TABLE IF EXISTS event_journal;


--changeset manoel.amaro:2
CREATE UNIQUE INDEX event_journal_ordering_idx ON public.event_journal(ordering);
-- rollback DROP INDEX event_journal_ordering_idx;


--changeset manoel.amaro:3
CREATE TABLE IF NOT EXISTS event_tag(
    event_id BIGINT,
    tag VARCHAR(256),
    PRIMARY KEY(event_id, tag),
    CONSTRAINT fk_event_journal
    FOREIGN KEY(event_id)
    REFERENCES event_journal(ordering)
    ON DELETE CASCADE
);
--rollback DROP TABLE IF EXISTS event_tag;

--changeset manoel.amaro:4
CREATE TABLE IF NOT EXISTS snapshot (
    persistence_id VARCHAR(255) NOT NULL,
    sequence_number BIGINT NOT NULL,
    created BIGINT NOT NULL,

    snapshot_ser_id INTEGER NOT NULL,
    snapshot_ser_manifest VARCHAR(255) NOT NULL,
    snapshot_payload BYTEA NOT NULL,

    meta_ser_id INTEGER,
    meta_ser_manifest VARCHAR(255),
    meta_payload BYTEA,

    PRIMARY KEY(persistence_id, sequence_number)
);
--rollback DROP TABLE IF EXISTS snapshot;

--changeset manoel.amaro:5
CREATE TABLE IF NOT EXISTS durable_state (
    global_offset BIGSERIAL,
    persistence_id VARCHAR(255) NOT NULL,
    revision BIGINT NOT NULL,
    state_payload BYTEA NOT NULL,
    state_serial_id INTEGER NOT NULL,
    state_serial_manifest VARCHAR(255),
    tag VARCHAR,
    state_timestamp BIGINT NOT NULL,
    PRIMARY KEY(persistence_id)
);
--rollback DROP TABLE IF EXISTS durable_state;

--changeset manoel.amaro:6
CREATE INDEX state_tag_idx on durable_state(tag);
--rollback DROP INDEX state_tag_idx;

--changeset manoel.amaro:7
CREATE INDEX state_global_offset_idx on durable_state(global_offset);
--rollback DROP INDEX state_global_offset_idx;

--changeset manoel.amaro:8
CREATE TABLE item_popularity(
    id VARCHAR(255) NOT NULL,
    version BIGINT NOT NULL,
    count INT NOT NULL,
    PRIMARY KEY(id)
);
--rollback DROP TABLE item_popularity;

--changeset manoel.amaro:9
CREATE TABLE IF NOT EXISTS akka_projection_offset_store (
    projection_name VARCHAR(255) NOT NULL,
    projection_key VARCHAR(255) NOT NULL,
    current_offset VARCHAR(255) NOT NULL,
    manifest VARCHAR(4) NOT NULL,
    mergeable BOOLEAN NOT NULL,
    last_updated BIGINT NOT NULL,
    PRIMARY KEY(projection_name, projection_key)
    );
--rollback drop table if exists akka_projection_offset_store;

--changeset manoel.amaro:10
CREATE INDEX IF NOT EXISTS projection_name_index ON akka_projection_offset_store (projection_name);
--rollback drop index if exists projection_name_index;


--changeset manoel.amaro:11
CREATE TABLE IF NOT EXISTS akka_projection_management (
    projection_name VARCHAR(255) NOT NULL,
    projection_key VARCHAR(255) NOT NULL,
    paused BOOLEAN NOT NULL,
    last_updated BIGINT NOT NULL,
    PRIMARY KEY(projection_name, projection_key)
);
--rollback drop table if exists akka_projection_management;
