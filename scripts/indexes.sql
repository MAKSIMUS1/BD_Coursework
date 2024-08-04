CREATE INDEX areas_id_btree_index 
ON public.areas USING btree (id)
TABLESPACE goalscoreservice_default;

CREATE INDEX areas_name_hash_index
ON public.areas USING hash (name)
TABLESPACE goalscoreservice_default;

CREATE INDEX idx_teams_name 
ON public.teams (name)
TABLESPACE goalscoreservice_default;

CREATE INDEX idx_teams_id 
ON public.teams (id)
TABLESPACE goalscoreservice_default;



--DROP INDEX IF EXISTS areas_name_hash_index;

EXPLAIN ANALYZE select * from public.areas where name = 'Area 80088';