CREATE USER test WITH
  LOGIN
  SUPERUSER
  INHERIT
  CREATEDB
  CREATEROLE
  REPLICATION
  PASSWORD 'password';

CREATE DATABASE "example";

-- use database example
\c "example";

SET search_path = example, pg_catalog;
SET default_tablespace = '';
SET default_with_oids = false;

CREATE SCHEMA example;

ALTER SCHEMA example OWNER TO test;

CREATE SEQUENCE example."user_id_seq"
    INCREMENT 1
    START 1000
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;
ALTER SEQUENCE example."user_id_seq" OWNER TO test;

CREATE TABLE example."user"
(
    id bigint NOT NULL DEFAULT nextval('example."user_id_seq"'::regclass),
    username text NOT NULL UNIQUE,
    password text NOT NULL,
    first_name text,
    last_name text,
    created_time timestamp with time zone NOT NULL DEFAULT timezone('GMT'::text, now()),
    CONSTRAINT "user_pkey" PRIMARY KEY (id)
);
ALTER TABLE example."user" OWNER to test;

-- insert some test data
INSERT INTO example.user ("username", "password", "first_name", "last_name", "created_time")
    VALUES ('bkuzmic', 'my_test_pass', 'Boris', 'Kuzmic', now());
INSERT INTO example.user ("username", "password", "first_name", "last_name", "created_time")
    VALUES ('alice', 'test123', 'Alice', 'Doe', now());
INSERT INTO example.user ("username", "password", "first_name", "last_name", "created_time")
    VALUES ('bob', 'test234', 'Bob', 'Doe', now());
