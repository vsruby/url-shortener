CREATE TABLE IF NOT EXISTS clicks (
    id UUID NOT NULL DEFAULT public.uuid_generate_v4(),
    short_url_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    CONSTRAINT pkey_clicks_id PRIMARY KEY (id),
    CONSTRAINT fkey_clicks_short_url_id FOREIGN KEY (short_url_id) REFERENCES short_urls (id)
);

CREATE INDEX key_clicks_short_url_id ON clicks (id);