
--ALTER TABLE public.token ALTER COLUMN id SET DEFAULT nextval('public.token_id_sequence'::regclass);
--ALTER TABLE public.token ALTER COLUMN id SET DEFAULT nextval('public.token_id_sequence'::regclass);
--ALTER TABLE public.team_on_match ALTER COLUMN id SET DEFAULT nextval('team_on_match_id_seq');
--ALTER TABLE public.match ALTER COLUMN id SET DEFAULT nextval('match_id_seq');
ALTER TABLE public.user_favorite_players ALTER COLUMN id SET DEFAULT nextval('user_favorite_players_id_seq');
ALTER TABLE public.user_star_team ALTER COLUMN id SET DEFAULT nextval('user_star_team_id_seq');


CREATE OR REPLACE FUNCTION get_user_star_team(_user_id INTEGER)
RETURNS TABLE (
    goalkeeper_id INTEGER,
    left_defender_id INTEGER,
    central_1_defender_id INTEGER,
    central_2_defender_id INTEGER,
    right_defender_id INTEGER,
    left_midfielder_id INTEGER,
    central_midfielder_id INTEGER,
    right_midfielder_id INTEGER,
    left_winger_id INTEGER,
    striker_id INTEGER,
    right_winger_id INTEGER
) AS $$
BEGIN 
    RETURN QUERY
   SELECT
        ust.goalkeeper_id,
        ust.left_defender_id,
        ust.central_1_defender_id,
        ust.central_2_defender_id,
        ust.right_defender_id,
        ust.left_midfielder_id,
        ust.central_midfielder_id,
        ust.right_midfielder_id,
        ust.left_winger_id,
        ust.striker_id,
        ust.right_winger_id
    FROM
        public.user_star_team ust
    WHERE
        ust.user_id = _user_id;

END;
$$ LANGUAGE plpgsql;

select * from public.get_user_star_team(24);


select * from public.get_player_by_id(1);

CREATE OR REPLACE FUNCTION get_player_by_id(in_player_id INTEGER)
RETURNS TABLE (
    id INTEGER,
    name VARCHAR(64),
    player_position VARCHAR(32),
    date_of_birth DATE,
    nationality CHARACTER(64),
    team_id INTEGER
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        public.players.id,
        public.players.name,
        public.players."position",
        public.players.date_of_birth,
        public.players.nationality,
        public.players.team_id
    FROM
        public.players
    WHERE
        public.players.id = in_player_id;
END;
$$ LANGUAGE plpgsql;






ALTER TABLE public.match
ADD CONSTRAINT check_match_datetime
CHECK (match_datetime <= now() + interval '5 minutes');
ALTER TABLE public.match
DROP CONSTRAINT check_match_datetime;

------------------------------------------ PROCEDURES
CREATE OR REPLACE PROCEDURE insert_new_team_on_match(
    _team_id INTEGER,
    _goalkeeper_id INTEGER,
    _left_defender_id INTEGER,
    _central_1_defender_id INTEGER,
    _central_2_defender_id INTEGER,
    _right_defender_id INTEGER,
    _left_midfielder_id INTEGER,
    _central_midfielder_id INTEGER,
    _right_midfielder_id INTEGER,
    _left_winger_id INTEGER,
    _striker_id INTEGER,
    _right_winger_id INTEGER,
	_comment text
) 
LANGUAGE 'plpgsql'
AS $PROC$
DECLARE
    team_ids INTEGER[];
    player_set INTEGER[];
BEGIN
    team_ids := ARRAY[_goalkeeper_id, _left_defender_id, _central_1_defender_id,
                     _central_2_defender_id, _right_defender_id, _left_midfielder_id,
                     _central_midfielder_id, _right_midfielder_id, _left_winger_id,
                     _striker_id, _right_winger_id];

	player_set := ARRAY(SELECT DISTINCT unnest(team_ids));

	IF array_length(team_ids, 1) <> array_length(player_set, 1) THEN
    RAISE EXCEPTION 'All player IDs must be unique';
	END IF;
	
    FOR i IN 1..array_length(team_ids, 1) LOOP
        IF _team_id <> (SELECT team_id FROM public.players WHERE id = team_ids[i]) THEN
            RAISE EXCEPTION 'team_id does not match for player with id %', team_ids[i];
        END IF;
    END LOOP;

    INSERT INTO public.team_on_match (
        team_id,
        goalkeeper_id,
        left_defender_id,
        central_1_defender_id,
        central_2_defender_id,
        right_defender_id,
        left_midfielder_id,
        central_midfielder_id,
        right_midfielder_id,
        left_winger_id,
        striker_id,
        right_winger_id,
		comment
    ) VALUES (
        _team_id,
        _goalkeeper_id,
        _left_defender_id,
        _central_1_defender_id,
        _central_2_defender_id,
        _right_defender_id,
        _left_midfielder_id,
        _central_midfielder_id,
        _right_midfielder_id,
        _left_winger_id,
        _striker_id,
        _right_winger_id,
		_comment
    );
END;
$PROC$;

-- match
CREATE OR REPLACE PROCEDURE insert_match(
    _competition_id integer,
    _attendance integer,
    _home_match_team_id integer,
    _away_match_team_id integer,
    _home_goal_score smallint,
    _away_goal_score smallint,
    _match_datetime timestamp
)
AS $PROC$
BEGIN
    IF _home_match_team_id = _away_match_team_id THEN
        RAISE EXCEPTION 'home_match_team_id cannot be equal to away_match_team_id';
    END IF;

    INSERT INTO public.match (
        competition_id,
        attendance,
        home_match_team_id,
        away_match_team_id,
        home_goal_score,
        away_goal_score,
        match_datetime
    )
    VALUES (
        _competition_id,
        _attendance,
        _home_match_team_id,
        _away_match_team_id,
        _home_goal_score,
        _away_goal_score,
        _match_datetime
    );
END;
$PROC$ LANGUAGE plpgsql;


-- new user
CREATE OR REPLACE PROCEDURE public.insert_new_user(
	IN _username text,
	IN _password text,
	IN _phone text,
	OUT user_id integer)
LANGUAGE 'plpgsql'
AS $PROC$
BEGIN
	user_id:=NULL::integer;
		 INSERT INTO public."user"(id, username, password, phone, join_date, role)
        VALUES(nextval('public.user_id_sequence'), _username, _password, _phone, now(),
               CASE WHEN currval('public.user_id_sequence') < 16 THEN 'ROLE_ADMIN'
                    ELSE 'ROLE_USER'
               END)
			RETURNING id INTO user_id;
	END;
$PROC$;

ALTER PROCEDURE public.insert_new_user(text, text, text, integer)
    OWNER TO postgres;
	
	
-- Добавление нового токена
CREATE OR REPLACE PROCEDURE public.insert_new_token(
	IN _user_id integer, 
	IN _token text)
LANGUAGE 'plpgsql'
AS $BODY$
DECLARE
	id_before_error integer;
	
	err_code text; 
	msg_text text;
    exc_context text; 
 	msg_detail text; 
 	exc_hint text; 
	BEGIN
	INSERT INTO public.token(user_id, token)
		VALUES(_user_id, _token);
			
	EXCEPTION WHEN OTHERS  
	THEN
	
		GET STACKED DIAGNOSTICS
    	err_code = RETURNED_SQLSTATE, 
		msg_text = MESSAGE_TEXT,
    	exc_context = PG_CONTEXT,
 		msg_detail = PG_EXCEPTION_DETAIL,
 		exc_hint = PG_EXCEPTION_HINT;
	--для выводы в postgree
    RAISE NOTICE 'ERROR CODE: % MESSAGE TEXT: % CONTEXT: % DETAIL: % HINT: %', 
   	err_code, msg_text, exc_context, msg_detail, exc_hint;
	--для вывода в приложении
	RAISE EXCEPTION 'ERROR CODE: % MESSAGE TEXT: % CONTEXT: % DETAIL: % HINT: %', 
   	err_code, msg_text, exc_context, msg_detail, exc_hint;
END;
$BODY$;
ALTER TABLE public."user"
ADD COLUMN role character varying(32) COLLATE pg_catalog."default";

-- Изменение токена по существующему пользователю
CREATE OR REPLACE PROCEDURE public.change_token(
	IN _user_id integer, 
	IN _token text)
LANGUAGE 'plpgsql'
AS $PROC$
DECLARE
	_id integer;
BEGIN
	_id := public.get_token_id_by_user_id(_user_id);
	IF _id = -1 THEN
        RAISE EXCEPTION 'no token';
    END IF;
	UPDATE public.token tr
	SET user_id = _user_id,
		token = _token
		WHERE id = _id;
END;
$PROC$;

-- установка изображения профиля
CREATE OR REPLACE PROCEDURE update_user_profile_image(
    _user_id integer,
    _profile_image bytea
)
AS $PROC$
BEGIN
    UPDATE public."user"
    SET profile_image = _profile_image
    WHERE id = _user_id;
END;
$PROC$ LANGUAGE plpgsql;

-- добавление избранного игрока
CREATE OR REPLACE PROCEDURE insert_user_favorite_player(
    p_user_id INTEGER,
    p_player_id INTEGER
)
AS $PROC$
BEGIN
    IF EXISTS (SELECT 1 FROM public.user_favorite_players WHERE user_id = p_user_id AND player_id = p_player_id) THEN
        RAISE EXCEPTION 'User % has already added Player % to favorites', p_user_id, p_player_id;
    ELSE
        INSERT INTO public.user_favorite_players (user_id, player_id)
        VALUES (p_user_id, p_player_id);
    END IF;
END;
$PROC$ LANGUAGE PLpgSQL;

-- добавление избранной команды
CREATE OR REPLACE PROCEDURE insert_user_favorite_team(
    p_user_id INTEGER,
    p_team_id INTEGER
)
AS $PROC$
BEGIN
    IF EXISTS (SELECT 1 FROM public.user_favorite_teams WHERE user_id = p_user_id AND team_id = p_team_id) THEN
        RAISE EXCEPTION 'User % has already added Team % to favorites', p_user_id, p_team_id;
    ELSE
        INSERT INTO public.user_favorite_teams (user_id, team_id)
        VALUES (p_user_id, p_team_id);
    END IF;
END;
$PROC$ LANGUAGE PLpgSQL;

-- удаление игрока из избранного пользователя
CREATE OR REPLACE PROCEDURE remove_favorite_player(
    p_user_id INTEGER,
    p_player_id INTEGER
)
AS $PROC$
BEGIN
    DELETE FROM public.user_favorite_players
    WHERE user_id = p_user_id AND player_id = p_player_id;
END;
$PROC$ LANGUAGE PLpgSQL;

-- удаление команды из избранного пользователя
CREATE OR REPLACE PROCEDURE remove_favorite_team(
    p_user_id INTEGER,
    p_team_id INTEGER
)
AS $PROC$
BEGIN
    DELETE FROM public.user_favorite_teams
    WHERE user_id = p_user_id AND team_id = p_team_id;
END;
$PROC$ LANGUAGE PLpgSQL;

-- построение уникальной команды пользователя
CREATE OR REPLACE PROCEDURE insert_user_star_team(
    p_user_id INTEGER,
    p_goalkeeper_id INTEGER,
    p_left_defender_id INTEGER,
    p_central_1_defender_id INTEGER,
    p_central_2_defender_id INTEGER,
    p_right_defender_id INTEGER,
    p_left_midfielder_id INTEGER,
    p_central_midfielder_id INTEGER,
    p_right_midfielder_id INTEGER,
    p_left_winger_id INTEGER,
    p_striker_id INTEGER,
    p_right_winger_id INTEGER
)
AS $PROC$
DECLARE
    team_ids INTEGER[];
    player_set INTEGER[];
BEGIN
    team_ids := ARRAY[p_goalkeeper_id, p_left_defender_id, p_central_1_defender_id,
                     p_central_2_defender_id, p_right_defender_id, p_left_midfielder_id,
                     p_central_midfielder_id, p_right_midfielder_id, p_left_winger_id,
                     p_striker_id, p_right_winger_id];

    player_set := ARRAY(SELECT DISTINCT unnest(team_ids));

    IF array_length(team_ids, 1) <> array_length(player_set, 1) THEN
        RAISE EXCEPTION 'All player IDs must be unique';
    END IF;

    INSERT INTO public.user_star_team (
        user_id,
        goalkeeper_id,
        left_defender_id,
        central_1_defender_id,
        central_2_defender_id,
        right_defender_id,
        left_midfielder_id,
        central_midfielder_id,
        right_midfielder_id,
        left_winger_id,
        striker_id,
        right_winger_id
    ) VALUES (
        p_user_id,
        p_goalkeeper_id,
        p_left_defender_id,
        p_central_1_defender_id,
        p_central_2_defender_id,
        p_right_defender_id,
        p_left_midfielder_id,
        p_central_midfielder_id,
        p_right_midfielder_id,
        p_left_winger_id,
        p_striker_id,
        p_right_winger_id
    );

END;
$PROC$ LANGUAGE plpgsql;


-- обновление команды пользователя
CREATE OR REPLACE PROCEDURE update_user_star_team(
    p_user_id INTEGER,
    p_goalkeeper_id INTEGER,
    p_left_defender_id INTEGER,
    p_central_1_defender_id INTEGER,
    p_central_2_defender_id INTEGER,
    p_right_defender_id INTEGER,
    p_left_midfielder_id INTEGER,
    p_central_midfielder_id INTEGER,
    p_right_midfielder_id INTEGER,
    p_left_winger_id INTEGER,
    p_striker_id INTEGER,
    p_right_winger_id INTEGER
)
AS $PROC$
DECLARE
    team_ids INTEGER[];
    player_set INTEGER[];
BEGIN
    team_ids := ARRAY[p_goalkeeper_id, p_left_defender_id, p_central_1_defender_id,
                     p_central_2_defender_id, p_right_defender_id, p_left_midfielder_id,
                     p_central_midfielder_id, p_right_midfielder_id, p_left_winger_id,
                     p_striker_id, p_right_winger_id];

    player_set := ARRAY(SELECT DISTINCT unnest(team_ids));

    IF array_length(team_ids, 1) <> array_length(player_set, 1) THEN
        RAISE EXCEPTION 'All player IDs must be unique';
    END IF;

    UPDATE public.user_star_team
    SET
        goalkeeper_id = p_goalkeeper_id,
        left_defender_id = p_left_defender_id,
        central_1_defender_id = p_central_1_defender_id,
        central_2_defender_id = p_central_2_defender_id,
        right_defender_id = p_right_defender_id,
        left_midfielder_id = p_left_midfielder_id,
        central_midfielder_id = p_central_midfielder_id,
        right_midfielder_id = p_right_midfielder_id,
        left_winger_id = p_left_winger_id,
        striker_id = p_striker_id,
        right_winger_id = p_right_winger_id
    WHERE user_id = p_user_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'User with user_id % not found', p_user_id;
    END IF;

END;
$PROC$ LANGUAGE plpgsql;


-- добавление команды
CREATE OR REPLACE PROCEDURE public.insert_team(
    p_id integer,
    p_area_id integer,
    p_name character varying(64),
    p_short_name character varying(32),
    p_tla character varying(8),
    p_address character varying(256),
    p_website character varying(128),
    p_founded integer,
    p_club_colors character varying(64)
)
LANGUAGE 'plpgsql'
AS $PROC$
BEGIN
    IF EXISTS (SELECT 1 FROM public.teams WHERE id = p_id) THEN
        RAISE EXCEPTION 'Team with id % already exists.', p_id;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM public.areas WHERE id = p_area_id) THEN
        RAISE EXCEPTION 'Invalid area_id: %.', p_area_id;
    END IF;

    IF p_founded > 2023 THEN
        RAISE EXCEPTION 'Invalid founded year: %, must be less than or equal to 2023.', p_founded;
    END IF;

    IF p_name = '' OR p_short_name = '' OR p_tla = '' OR p_address = '' OR p_website = '' OR p_club_colors = '' THEN
        RAISE EXCEPTION 'All string parameters must be non-empty.';
    END IF;

    INSERT INTO public.teams (
        id, area_id, name, short_name, tla,
        address, website, founded, club_colors
    ) VALUES (
        p_id, p_area_id, p_name, p_short_name, p_tla,
        p_address, p_website, p_founded, p_club_colors
    );
END;
$PROC$;


-- обновление процедуры
CREATE OR REPLACE PROCEDURE public.update_team(
    p_id integer,
    p_area_id integer,
    p_name character varying(64),
    p_short_name character varying(32),
    p_tla character varying(8),
    p_address character varying(256),
    p_website character varying(128),
    p_founded integer,
    p_club_colors character varying(64)
)
LANGUAGE 'plpgsql'
AS $PROC$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM public.areas WHERE id = p_area_id) THEN
        RAISE EXCEPTION 'Invalid area_id: %.', p_area_id;
    END IF;

    IF p_founded > 2023 THEN
        RAISE EXCEPTION 'Invalid founded year: %, must be less than or equal to 2023.', p_founded;
    END IF;

    IF p_name = '' OR p_short_name = '' OR p_tla = '' OR p_address = '' OR p_website = '' OR p_club_colors = '' THEN
        RAISE EXCEPTION 'All string parameters must be non-empty.';
    END IF;
    UPDATE public.teams
    SET
        area_id = p_area_id,
        name = p_name,
        short_name = p_short_name,
        tla = p_tla,
        address = p_address,
        website = p_website,
        founded = p_founded,
        club_colors = p_club_colors
    WHERE id = p_id;
END;
$PROC$;

-- удаление команды
CREATE OR REPLACE PROCEDURE public.delete_team(
    p_team_id integer
)
LANGUAGE 'plpgsql'
AS $PROC$
DECLARE
    player_count integer;
    team_on_match_count integer;
    player_ids integer[];
BEGIN
	IF NOT EXISTS (SELECT 1 FROM public.teams WHERE id = p_team_id) THEN
        RAISE EXCEPTION 'Team with id % does not exist.', p_team_id;
    END IF;
	
    SELECT COUNT(*) INTO player_count
    FROM public.players
    WHERE team_id = p_team_id;

    IF player_count > 0 THEN
        DELETE FROM public.user_favorite_players
        WHERE player_id IN (SELECT id FROM public.players WHERE team_id = p_team_id);

        DELETE FROM public.players
        WHERE team_id = p_team_id;
    END IF;

    SELECT COUNT(*) INTO team_on_match_count
    FROM public.team_on_match
    WHERE team_id = p_team_id;

    IF team_on_match_count > 0 THEN
        DELETE FROM public.team_on_match
        WHERE team_id = p_team_id;
    END IF;

    DELETE FROM public.teams
    WHERE id = p_team_id;
END;
$PROC$;


-- добавление игрока
CREATE OR REPLACE PROCEDURE public.insert_player(
    p_id integer,
    p_name character varying(64),
    p_position character varying(32),
    p_date_of_birth date,
    p_nationality character(64),
    p_team_id integer
)
LANGUAGE 'plpgsql'
AS $PROC$
BEGIN
	IF EXISTS (SELECT 1 FROM public.players WHERE id = p_id) THEN
        RAISE EXCEPTION 'Player with id % already exists', p_id;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM public.teams WHERE id = p_team_id) THEN
        RAISE EXCEPTION 'Team with id % not found', p_team_id;
    END IF;

    IF p_date_of_birth > CURRENT_DATE THEN
        RAISE EXCEPTION 'Date of birth cannot be in the future';
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM public.areas
        WHERE name = p_nationality
    ) THEN
        RAISE EXCEPTION 'Nationality % not found in areas table', p_nationality;
    END IF;

    CASE
        WHEN p_position IN ('Goalkeeper', 'Defence', 'Midfield', 'Offence') THEN
            -- ok
        ELSE
            RAISE EXCEPTION 'Invalid position: %', p_position;
    END CASE;
	
    INSERT INTO public.players (id, name, "position", date_of_birth, nationality, team_id)
    VALUES (p_id, p_name, p_position, p_date_of_birth, p_nationality, p_team_id);
END;
$PROC$;

select * from players;
select * from public.areas;

-- обновленияв таблице players
CREATE OR REPLACE PROCEDURE public.update_player(
    p_id integer,
    p_name character varying(64),
    p_position character varying(32),
    p_date_of_birth date,
    p_nationality character(64),
    p_team_id integer
)
LANGUAGE 'plpgsql'
AS $PROC$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM public.teams WHERE id = p_team_id) THEN
        RAISE EXCEPTION 'Team with id % not found', p_team_id;
    END IF;

    IF p_date_of_birth > CURRENT_DATE THEN
        RAISE EXCEPTION 'Date of birth cannot be in the future';
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM public.areas
        WHERE name = p_nationality
    ) THEN
        RAISE EXCEPTION 'Nationality % not found in areas table', p_nationality;
    END IF;

	CASE
        WHEN p_position IN ('Goalkeeper', 'Defence', 'Midfield', 'Offence') THEN
            -- ok
        ELSE
            RAISE EXCEPTION 'Invalid position: %', p_position;
    END CASE;
	
    UPDATE public.players
    SET name = p_name, "position" = p_position,
        date_of_birth = p_date_of_birth, nationality = p_nationality, team_id = p_team_id
    WHERE id = p_id;
END;
$PROC$;

-- удаления из таблицы players
CREATE OR REPLACE PROCEDURE public.delete_player(
    p_player_id integer
)
LANGUAGE 'plpgsql'
AS $PROC$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM public.players WHERE id = p_player_id) THEN
        RAISE EXCEPTION 'Player with id % not found', p_player_id;
    END IF;

    DELETE FROM public.players WHERE id = p_player_id;
END;
$PROC$;

-- зпалонить area 100000 строк
CREATE OR REPLACE PROCEDURE public.populate_areas()
LANGUAGE 'plpgsql'
AS $PROC$
DECLARE
    start_id INTEGER := 3000;
    end_id INTEGER := 103000;
BEGIN
    FOR i IN start_id..end_id LOOP
        INSERT INTO public.areas (id, name)
        VALUES (i, 'Area ' || i);
    END LOOP;
END;
$PROC$;


--|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|--
--------------------------------------- ~~~~~~~~~ ---------------------------------------
--------------------------------------- FUNCTIONS ---------------------------------------
--------------------------------------- ~~~~~~~~~ ---------------------------------------
--|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|--


-- получить id токена с user_id
CREATE OR REPLACE FUNCTION public.register_user(
	IN _username text,
	IN _password text,
	IN _phone text) 
	RETURNS integer AS
$FUNC$
DECLARE _user_id integer;
BEGIN  
    call public.insert_new_user(_username, _password, _phone, _user_id);
	RETURN _user_id;
 END;
$FUNC$
LANGUAGE plpgsql;
-- получить id токена с user_id
CREATE OR REPLACE FUNCTION public.get_token_id_by_user_id(integer) RETURNS integer AS
$FUNC$
BEGIN  
    IF ((SELECT id FROM public.token WHERE user_id=$1) IS NOT NULL) THEN
    	RETURN (SELECT id FROM public.token WHERE user_id=$1);
    END IF;
	RETURN -1;
 END;
$FUNC$
LANGUAGE plpgsql;
-- Существует ли токен с user_id
CREATE OR REPLACE FUNCTION public.is_token_with_user_id(integer) RETURNS boolean AS
$FUNC$
BEGIN  
    IF ((SELECT COUNT(*) FROM public.token WHERE user_id=$1) = 1) THEN
    	RETURN TRUE;
    END IF;
	RETURN FALSE;
 END;
$FUNC$
LANGUAGE plpgsql;

-- Поулчение пользователя по логину
CREATE OR REPLACE FUNCTION public.find_user_by_login(text) RETURNS SETOF public.user AS
$FUNC$
BEGIN
    RETURN QUERY SELECT *
                   FROM public.user
                  WHERE username = $1;
				  
    IF NOT FOUND THEN
        RAISE EXCEPTION 'no user with such login: %.', $1;
    END IF;

    RETURN;
 END;
$FUNC$
LANGUAGE plpgsql;

-- поулчение роли пользователя по токену
CREATE OR REPLACE FUNCTION get_user_role_by_token(input_token character varying)
RETURNS character varying AS
$FUNC$
DECLARE
    user_role character varying;
BEGIN
    SELECT u.role INTO user_role
    FROM public."user" u
    JOIN public.token t ON u.id = t.user_id
    WHERE t.token = input_token;

    RETURN user_role;
END;
$FUNC$
LANGUAGE plpgsql;

-- получение всех матчей
CREATE OR REPLACE FUNCTION get_all_matches()
RETURNS TABLE (
    match_id integer,
    competition_id integer,
    attendance integer,
    home_match_team_id integer,
    away_match_team_id integer,
    home_goal_score smallint,
    away_goal_score smallint,
    match_datetime timestamp without time zone
)
AS $FUNC$
BEGIN
   RETURN QUERY SELECT
        m.id AS match_id,
        m.competition_id,
        m.attendance,
        m.home_match_team_id,
        m.away_match_team_id,
        m.home_goal_score,
        m.away_goal_score,
        m.match_datetime
    FROM
        public.match m;
END;
$FUNC$ LANGUAGE plpgsql;

-- info about mathces
CREATE OR REPLACE FUNCTION get_matches_info()
RETURNS TABLE (
    match_id integer,
    competition_name character varying(64),
    area_name character varying(32),
    match_datetime timestamp without time zone,
    home_team_name character varying(64),
    home_team_id integer,
    away_team_name character varying(64),
    away_team_id integer,
    home_goal smallint,
    away_goal smallint
)
AS $FUNC$
BEGIN
    RETURN QUERY SELECT
        m.id AS match_id,
        c.name AS competition_name,
        a.name AS area_name,
        m.match_datetime,
        home_team.name AS home_team_name,
        home_team.id AS home_team_id,
        away_team.name AS away_team_name,
        away_team.id AS away_team_id,
        m.home_goal_score as home_goal,
        m.away_goal_score as away_goal
    FROM
        public.match m
        JOIN public.competitions c ON m.competition_id = c.id
        JOIN public.areas a ON c.area_id = a.id
        JOIN public.team_on_match tom_home ON m.home_match_team_id = tom_home.id
        JOIN public.teams home_team ON tom_home.team_id = home_team.id
        JOIN public.team_on_match tom_away ON m.away_match_team_id = tom_away.id
        JOIN public.teams away_team ON tom_away.team_id = away_team.id;
END;
$FUNC$ LANGUAGE plpgsql;

-- плучение пользователя по токену
CREATE OR REPLACE FUNCTION get_user_id_by_token(_token character varying)
RETURNS integer
AS $FUNC$
DECLARE
    _user_id integer;
BEGIN
    SELECT user_id INTO _user_id
    FROM public.token
    WHERE token = _token;

    RETURN _user_id;
END;
$FUNC$ LANGUAGE plpgsql;


-- получить пользователя по его id
CREATE OR REPLACE FUNCTION get_user_by_id(_user_id integer)
RETURNS TABLE (
    id integer,
    username character varying(32),
    password character varying(256),
    favorite_team_id integer,
    profile_image bytea,
    phone character varying(16),
    join_date date,
    role character varying(32)
)
AS $FUNC$
BEGIN
    RETURN QUERY SELECT
        u.id,
        u.username,
        u.password,
        u.favorite_team_id,
        u.profile_image,
        u.phone,
        u.join_date,
        u.role
    FROM
        public."user" u
    WHERE
        u.id = _user_id;
END;
$FUNC$ LANGUAGE plpgsql;

-- поиск матчей, по названиям команд
CREATE OR REPLACE FUNCTION get_matches_info_by_team_names(search_query varchar)
RETURNS TABLE (
    match_id INTEGER,
    competition_name VARCHAR(64),
    area_name VARCHAR(32),
    match_datetime TIMESTAMP,
    home_team_name VARCHAR(64),
    home_team_id INTEGER,
    away_team_name VARCHAR(64),
    away_team_id INTEGER,
    home_goal SMALLINT,
    away_goal SMALLINT
)
AS $FUNC$
BEGIN
    RETURN QUERY
    SELECT
        m.id AS match_id,
        c.name AS competition_name,
        a.name AS area_name,
        m.match_datetime,
        home_team.name AS home_team_name,
        home_team.id AS home_team_id,
        away_team.name AS away_team_name,
        away_team.id AS away_team_id,
        m.home_goal_score AS home_goal,
        m.away_goal_score AS away_goal
    FROM
        public.match m
        JOIN public.competitions c ON m.competition_id = c.id
        JOIN public.areas a ON c.area_id = a.id
        JOIN public.team_on_match tom_home ON m.home_match_team_id = tom_home.id
        JOIN public.teams home_team ON tom_home.team_id = home_team.id
        JOIN public.team_on_match tom_away ON m.away_match_team_id = tom_away.id
        JOIN public.teams away_team ON tom_away.team_id = away_team.id
    WHERE
        home_team.name ILIKE '%' || search_query || '%'
        OR away_team.name ILIKE '%' || search_query || '%';
END;
$FUNC$ LANGUAGE plpgsql;

--получить информацию о матче по его id
	CREATE OR REPLACE FUNCTION get_match_info_by_id(_match_id integer)
RETURNS TABLE (
    match_id integer,
    competition_name character varying(64),
    area_name character varying(32),
    match_datetime timestamp without time zone,
    home_team_name character varying(64),
    home_team_id integer,
    away_team_name character varying(64),
    away_team_id integer,
    home_goal smallint,
    away_goal smallint,
    attendance integer
)
AS $FUNC$
BEGIN
    RETURN QUERY SELECT
        m.id AS match_id,
        c.name AS competition_name,
        a.name AS area_name,
        m.match_datetime,
        home_team.name AS home_team_name,
        home_team.id AS home_team_id,
        away_team.name AS away_team_name,
        away_team.id AS away_team_id,
        m.home_goal_score as home_goal,
        m.away_goal_score as away_goal,
        m.attendance
    FROM
        public.match m
        JOIN public.competitions c ON m.competition_id = c.id
        JOIN public.areas a ON c.area_id = a.id
        JOIN public.team_on_match tom_home ON m.home_match_team_id = tom_home.id
        JOIN public.teams home_team ON tom_home.team_id = home_team.id
        JOIN public.team_on_match tom_away ON m.away_match_team_id = tom_away.id
        JOIN public.teams away_team ON tom_away.team_id = away_team.id
    WHERE m.id = _match_id;
END;
$FUNC$ LANGUAGE plpgsql;

-- полученить игроков старта
CREATE OR REPLACE FUNCTION get_starting_players(team_on_match_id INTEGER)
RETURNS TABLE (
    player_id INTEGER,
    player_name CHARACTER VARYING(64),
    player_position TEXT
) AS $FUNC$
BEGIN
    RETURN QUERY
    SELECT DISTINCT ON (p.id)
        p.id AS player_id,
        p.name AS player_name,
        CASE
            WHEN tm.goalkeeper_id = p.id THEN 'goalkeeper'
            WHEN tm.left_defender_id = p.id THEN 'left defender'
            WHEN tm.central_1_defender_id = p.id THEN 'central 1 defender'
            WHEN tm.central_2_defender_id = p.id THEN 'central 2 defender'
            WHEN tm.right_defender_id = p.id THEN 'right defender'
            WHEN tm.left_midfielder_id = p.id THEN 'left midfielder'
            WHEN tm.central_midfielder_id = p.id THEN 'central midfielder'
            WHEN tm.right_midfielder_id = p.id THEN 'right midfielder'
            WHEN tm.left_winger_id = p.id THEN 'left winger'
            WHEN tm.striker_id = p.id THEN 'striker'
            WHEN tm.right_winger_id = p.id THEN 'right winger'
        END AS player_position
    FROM
        team_on_match tm
    INNER JOIN players p ON tm.team_id = p.team_id
    WHERE
        tm.id = team_on_match_id;

    RETURN;
END;
$FUNC$ LANGUAGE plpgsql;

-- полчуение матча по его id
CREATE OR REPLACE FUNCTION get_match_by_id(p_match_id INTEGER)
RETURNS TABLE (
    match_id INTEGER,
    competition_id INTEGER,
    attendance INTEGER,
    home_match_team_id INTEGER,
    away_match_team_id INTEGER,
    home_goal_score SMALLINT,
    away_goal_score SMALLINT,
    match_datetime TIMESTAMP WITHOUT TIME ZONE
) AS $FUNC$
BEGIN
    RETURN QUERY
    SELECT
        m.id AS match_id,
        m.competition_id,
        m.attendance,
        m.home_match_team_id,
        m.away_match_team_id,
        m.home_goal_score,
        m.away_goal_score,
        m.match_datetime
    FROM
        public.match m
    WHERE
        m.id = p_match_id;

    RETURN;
END;
$FUNC$ LANGUAGE plpgsql;

-- получение команды по id
CREATE OR REPLACE FUNCTION get_team_by_id(team_id INTEGER)
RETURNS TABLE (
    id INTEGER,
    area_id INTEGER,
    name VARCHAR(64),
    short_name VARCHAR(32),
    tla VARCHAR(8),
    address VARCHAR(256),
    website VARCHAR(128),
    founded INTEGER,
    club_colors VARCHAR(64)
)
AS $FUNC$
BEGIN
    RETURN QUERY
    SELECT
        t.id,
        t.area_id,
        t.name,
        t.short_name,
        t.tla,
        t.address,
        t.website,
        t.founded,
        t.club_colors
    FROM
        public.teams t
    WHERE
        t.id = get_team_by_id.team_id;
END;
$FUNC$ LANGUAGE PLPGSQL;

--получение заявки команды по id
CREATE OR REPLACE FUNCTION get_team_on_match_by_id(team_on_match_id INTEGER)
RETURNS TABLE (
    id INTEGER,
    team_id INTEGER,
    goalkeeper_id INTEGER,
    left_defender_id INTEGER,
    central_1_defender_id INTEGER,
    central_2_defender_id INTEGER,
    right_defender_id INTEGER,
    left_midfielder_id INTEGER,
    central_midfielder_id INTEGER,
    right_midfielder_id INTEGER,
    left_winger_id INTEGER,
    striker_id INTEGER,
    right_winger_id INTEGER,
    comment VARCHAR(256)
)
AS $FUNC$
BEGIN
    RETURN QUERY
    SELECT
        tm.id,
        tm.team_id,
        tm.goalkeeper_id,
        tm.left_defender_id,
        tm.central_1_defender_id,
        tm.central_2_defender_id,
        tm.right_defender_id,
        tm.left_midfielder_id,
        tm.central_midfielder_id,
        tm.right_midfielder_id,
        tm.left_winger_id,
        tm.striker_id,
        tm.right_winger_id,
        tm.comment
    FROM
        public.team_on_match tm
    WHERE
        tm.id = get_team_on_match_by_id.team_on_match_id;
END;
$FUNC$ LANGUAGE PLPGSQL;

-- получение избранных игроков по id user
CREATE OR REPLACE FUNCTION get_favorite_players_data(p_user_id INTEGER)
RETURNS TABLE (
    player_id INTEGER,
    player_name CHARACTER VARYING(64),
    player_position CHARACTER VARYING(32),
    player_date_of_birth DATE,
    player_nationality CHARACTER(64),
    team_short_name CHARACTER VARYING(32),
    team_id INTEGER
)
AS $FUNC$
BEGIN
    RETURN QUERY
    SELECT
        p.id AS player_id,
        p.name AS player_name,
        p."position" AS player_position,
        p.date_of_birth AS player_date_of_birth,
        p.nationality AS player_nationality,
        t.short_name AS team_short_name,
		t.id AS team_id
    FROM
        public.user_favorite_players ufp
    JOIN
        public.players p ON ufp.player_id = p.id
    JOIN
        public.teams t ON p.team_id = t.id
    WHERE
        ufp.user_id = p_user_id;
END;
$FUNC$ LANGUAGE plpgsql;

-- проверка на существование созданна ли пользовательякая команда
CREATE OR REPLACE FUNCTION check_user_star_team_exists(p_user_id INTEGER)
RETURNS BOOLEAN AS $FUNC$
DECLARE
    user_exists BOOLEAN;
BEGIN
    SELECT TRUE INTO user_exists
    FROM public.user_star_team
    WHERE user_id = p_user_id
    LIMIT 1;

    RETURN user_exists;
END;
$FUNC$ LANGUAGE plpgsql;

-- функция поиска матчей
CREATE OR REPLACE FUNCTION public.get_searched_matches_info(
    IN team_search character varying
)
RETURNS TABLE(match_id integer, 
			  competition_name character varying, 
			  area_name character varying, 
			  match_datetime timestamp without time zone, 
			  home_team_name character varying, 
			  home_team_id integer,
			  away_team_name character varying, 
			  away_team_id integer, 
			  home_goal smallint, 
			  away_goal smallint) 
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY SELECT
        m.id AS match_id,
        c.name AS competition_name,
        a.name AS area_name,
        m.match_datetime,
        home_team.name AS home_team_name,
        home_team.id AS home_team_id,
        away_team.name AS away_team_name,
        away_team.id AS away_team_id,
        m.home_goal_score as home_goal,
        m.away_goal_score as away_goal
    FROM
        public.match m
        JOIN public.competitions c ON m.competition_id = c.id
        JOIN public.areas a ON c.area_id = a.id
        JOIN public.team_on_match tom_home ON m.home_match_team_id = tom_home.id
        JOIN public.teams home_team ON tom_home.team_id = home_team.id
        JOIN public.team_on_match tom_away ON m.away_match_team_id = tom_away.id
        JOIN public.teams away_team ON tom_away.team_id = away_team.id
    WHERE
        team_search IS NULL 
        OR home_team.name ILIKE '%' || team_search || '%' 
        OR away_team.name ILIKE '%' || team_search || '%';
END;
$BODY$;

-- получение игроков в диапазоне 
CREATE OR REPLACE FUNCTION public.get_players_range(start_index INT)
RETURNS TABLE(player_id INT, player_name VARCHAR, player_position VARCHAR, player_date_of_birth DATE, player_nationality VARCHAR, team_short_name VARCHAR, team_id INT)
LANGUAGE plpgsql
AS $FUNC$
BEGIN
    RETURN QUERY
    SELECT
        p.id::INT AS player_id,
        p.name::VARCHAR AS player_name,
        p."position"::VARCHAR AS player_position,
        p.date_of_birth::DATE AS player_date_of_birth,
        p.nationality::VARCHAR AS player_nationality,
        t.short_name::VARCHAR AS team_short_name,
        t.id::INT AS team_id
    FROM
        public.players p
        JOIN public.teams t ON p.team_id = t.id
    ORDER BY
        p.id
    OFFSET start_index
    LIMIT 100;
END;
$FUNC$;

-- получить количество страниц пагинации игроков
CREATE OR REPLACE FUNCTION public.get_total_player_pages()
RETURNS INTEGER
LANGUAGE plpgsql
AS $FUNC$
DECLARE
    total_players INTEGER;
    players_per_page INTEGER := 100;
BEGIN
    SELECT COUNT(*) INTO total_players FROM public.players;

    RETURN CEIL(total_players / players_per_page) + 1;
END;
$FUNC$;


-- получение района по id 
CREATE OR REPLACE FUNCTION public.get_area_by_id(
    p_area_id integer
)
RETURNS TABLE(
    area_id integer,
    area_name character varying
)
LANGUAGE 'plpgsql'
AS $FUNC$
BEGIN
    RETURN QUERY
    SELECT
        id AS area_id,
        name AS area_name
    FROM
        public.areas
    WHERE
        id = p_area_id;
END;
$FUNC$;

-- получение избранных команд пользователя
CREATE OR REPLACE FUNCTION get_favorite_teams_by_user_id(p_user_id INTEGER)
RETURNS TABLE (
    favorite_team_id INTEGER,
    user_id INTEGER,
    team_id INTEGER
)
AS $FUNC$
BEGIN
    RETURN QUERY
    SELECT
        uft.id AS favorite_team_id,
        uft.user_id,
        uft.team_id
    FROM
        public.user_favorite_teams uft
    WHERE
        uft.user_id = p_user_id;
END;
$FUNC$ LANGUAGE plpgsql;


------------------------------------ ТЕСТИРОВАНИЕ ФУНКЦИЙ И ПРОЦЕДУР
-- drop functions
select nextval('public.user_id_sequence');
select currval('public.user_id_sequence');
select nextval('public.token_id_sequence');
select currval('public.token_id_sequence');
SELECT last_value FROM public.token_id_sequence;
-- TESTS OF PROCEDURES AND FUNCTIONS
DO $$
DECLARE r bigint;
BEGIN
SELECT last_value FROM public.token_id_sequence
END$$;

select is_token_with_user_id(17);
select is_token_with_user_id(179);

select public.get_token_id_by_user_id(17);
select public.get_token_id_by_user_id(179);
select * from public.register_user('gkljhkjhh', 'password_of_rosomaha', 'phoneepta');
call public.insert_new_user('aaaghjti', 'password_of_rosomaha', 'phoneepta', null);
call public.insert_new_user('rosofdfhmahs228a', 'password_of_rosomaha', 'phoneepta');
call public.insert_new_token(269, 'GUID_TEST');
call public.change_token(179, 'NEW_TOKEN_TEST');
select * from public.token;
select * from public.user;
SELECT * FROM public.find_user_by_login('tecjdtlogin');
SELECT * FROM public.find_user_by_login('sdfsdf');
ALTER SEQUENCE public.user_id_sequence RESTART WITH 16;
SELECT * FROM public.find_user_by_login('no_user_with_such_login');
select * from public.get_all_teams();
--delete from token;
--delete from public.user;
select * from public.register_user(NULL,'lkjhkljhkjh', 'lkjhkljhkjh', 'lkjhk') as result

select * from teams;
select * from match;

select * from public.get_team_by_id(86);

select * from public.get_starting_players(12);
select * from get_match_by_id(4);

select * from public.user_favorite_players;
--delete from public.user_favorite_players;

select * from public.user_star_team;


select * from public.get_favorite_players_data(24);
select * from public.players;
select * from public.get_matches_info_by_team_names('real');
select * from public.get_match_info_by_id(4);
select * from public.get_matches_info();
select * from competitions;
select * from team_on_match;
select * from teams where name like 'Man%';
select * from players where team_id = 65;
SELECT * FROM get_all_matches();
select * from match;
select * from teams where name like 'Real Madrid%';
select * from players where team_id = 86 order by position;

select * from team_on_match;
select * from competitions;

select * from public.get_searched_matches_info('a');

select * from teams where name like 'AC Milan';
select * from players where team_id = 98 order by position;

select * from public.user_favorite_teams;
select * from public.user;
select * from teams where name like 'FC Internazionale Milano';
select * from players where team_id = 108 order by position;
select * from public.get_favorite_teams_by_user_id(24);
SELECT * FROM public.get_players_range(0);
Select * from public.user_favorite_teams;





select * from teams;

select * from public.areas;
delete from public.areas where id = 3000;
call public.populate_areas();


select * from public.areas;

EXPLAIN ANALYZE SELECT * FROM public.areas WHERE id = 80888;

















------