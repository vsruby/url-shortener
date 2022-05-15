CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;

CREATE TABLE IF NOT EXISTS short_urls (
    id UUID NOT NULL DEFAULT public.uuid_generate_v4(),
    code VARCHAR(16) NOT NULL,
    destination TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    CONSTRAINT pkey_short_urls_id PRIMARY KEY (id)
);

CREATE UNIQUE INDEX key_short_urls_code ON short_urls (code);