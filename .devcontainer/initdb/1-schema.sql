CREATE TABLE blogpost (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    title text NOT NULL,
    body text NOT NULL,
    last_updated timestamp(0) with time zone DEFAULT now() NOT NULL,
    published timestamp(0) with time zone DEFAULT now() NOT NULL
);

CREATE INDEX idx_published ON blogpost USING btree (published DESC NULLS LAST);
