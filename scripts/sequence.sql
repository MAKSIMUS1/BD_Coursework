CREATE  SEQUENCE IF NOT EXISTS public.user_id_sequence
    INCREMENT 1
    START 16
    MINVALUE 16
    MAXVALUE 2147483640
    CACHE 8
    OWNED BY "user".id;

ALTER SEQUENCE public.user_id_sequence
    OWNER TO postgres;

CREATE SEQUENCE IF NOT EXISTS public.token_id_sequence
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483640
    CACHE 8
    OWNED BY "token".id;

ALTER SEQUENCE public.token_id_sequence
    OWNER TO postgres;

CREATE SEQUENCE team_on_match_id_seq START 1;

ALTER SEQUENCE public.team_on_match_id_seq
    OWNER TO postgres;

CREATE SEQUENCE match_id_seq START 1;

ALTER SEQUENCE public.match_id_seq
    OWNER TO postgres;

CREATE SEQUENCE user_favorite_players_id_seq START 1;

ALTER SEQUENCE public.user_favorite_players_id_seq
    OWNER TO postgres;

CREATE SEQUENCE user_favorite_teams_id_seq START 1;

ALTER SEQUENCE public.user_favorite_teams_id_seq
    OWNER TO postgres;
	
CREATE SEQUENCE user_star_team_id_seq START 1;

ALTER SEQUENCE public.user_star_team_id_seq
    OWNER TO postgres;
	
--DROP SEQUENCE IF EXISTS public.user_id_sequence;
--DROP SEQUENCE IF EXISTS public.token_id_sequence;
