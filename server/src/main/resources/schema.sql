CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS hstore;

CREATE TABLE IF NOT EXISTS public.page_embeddings
(
    id                 serial primary key,
    content            text,
    local_embeddings   vector(1024),
    mistral_embeddings vector(1024),
    page_id            integer
        constraint page_embeddings_pages_id_fk
            references public.pages on delete cascade
);

CREATE INDEX IF NOT EXISTS page_embeddings_local_embeddings_idx
    ON public.page_embeddings USING hnsw (local_embeddings public.vector_cosine_ops);

CREATE INDEX IF NOT EXISTS page_embeddings_mistral_embeddings_idx
    ON public.page_embeddings USING hnsw (mistral_embeddings public.vector_cosine_ops);
