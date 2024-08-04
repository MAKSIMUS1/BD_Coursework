-- Обновляемые json-файлы в проекте C#
-- areas
CREATE OR REPLACE PROCEDURE public.import_from_json_areas(path_to_file text)
LANGUAGE 'plpgsql'
AS $BODY$
DECLARE
	json_string	jsonb;
	
	err_code text;
	msg_text text;
	exc_context text;
	msg_detail text;
 	exc_hint text;
BEGIN
	json_string := pg_read_file(path_to_file);
	insert into public.areas 
	select (obj->>'id')::integer as id, (obj->>'name') as name 
	from jsonb_array_elements(json_string) as obj;
	
	RAISE NOTICE 'Success';
	RAISE NOTICE 'Value of path_to_file: %', path_to_file;
	RAISE NOTICE 'Value of json_string: %', json_string;
	
	EXCEPTION WHEN OTHERS  
	THEN
		GET STACKED DIAGNOSTICS
    	err_code = RETURNED_SQLSTATE,
		msg_text = MESSAGE_TEXT,
    	exc_context = PG_CONTEXT,
 		msg_detail = PG_EXCEPTION_DETAIL,
 		exc_hint = PG_EXCEPTION_HINT;

    RAISE NOTICE 
	'ERROR CODE: % 
	MESSAGE TEXT: % 
	CONTEXT: % 
	DETAIL: % 
	HINT: %', 
   	err_code, msg_text, exc_context, msg_detail, exc_hint;
END;
$BODY$;

-- competitions
CREATE OR REPLACE PROCEDURE public.import_from_json_competitions(path_to_file text)
LANGUAGE 'plpgsql'
AS $BODY$
DECLARE
	json_string	jsonb;
	
	err_code text;
	msg_text text;
	exc_context text;
	msg_detail text;
 	exc_hint text;
BEGIN
	json_string := pg_read_file(path_to_file);
	insert into public.competitions 
	select (obj->>'id')::integer as id, (obj->>'area_id')::integer as area_id, (obj->>'name') as name 
	from jsonb_array_elements(json_string) as obj;
	
	RAISE NOTICE 'Success';
	RAISE NOTICE 'Value of path_to_file: %', path_to_file;
	RAISE NOTICE 'Value of json_string: %', json_string;
	
	EXCEPTION WHEN OTHERS  
	THEN
		GET STACKED DIAGNOSTICS
    	err_code = RETURNED_SQLSTATE,
		msg_text = MESSAGE_TEXT,
    	exc_context = PG_CONTEXT,
 		msg_detail = PG_EXCEPTION_DETAIL,
 		exc_hint = PG_EXCEPTION_HINT;

    RAISE NOTICE 
	'ERROR CODE: % 
	MESSAGE TEXT: % 
	CONTEXT: % 
	DETAIL: % 
	HINT: %', 
   	err_code, msg_text, exc_context, msg_detail, exc_hint;
END;
$BODY$;

-- teams
CREATE OR REPLACE PROCEDURE public.import_from_json_teams(path_to_file text)
LANGUAGE 'plpgsql'
AS $BODY$
DECLARE
	json_string	jsonb;
	
	err_code text;
	msg_text text;
	exc_context text;
	msg_detail text;
 	exc_hint text;
BEGIN
	json_string := pg_read_file(path_to_file);
	insert into public.teams
	select (obj->>'id')::integer as id, 
	(obj->>'area_id')::integer as area_id, 
	(obj->>'name') as name, 
	(obj->>'short_name') as short_name, 
	(obj->>'tla') as tla, 
	(obj->>'address') as address, 
	(obj->>'website') as website, 
	(obj->>'founded')::integer as founded, 
	(obj->>'club_colors') as club_colors
	from jsonb_array_elements(json_string) as obj;
	
	RAISE NOTICE 'Success';
	RAISE NOTICE 'Value of path_to_file: %', path_to_file;
	RAISE NOTICE 'Value of json_string: %', json_string;
	
	EXCEPTION WHEN OTHERS  
	THEN
		GET STACKED DIAGNOSTICS
    	err_code = RETURNED_SQLSTATE,
		msg_text = MESSAGE_TEXT,
    	exc_context = PG_CONTEXT,
 		msg_detail = PG_EXCEPTION_DETAIL,
 		exc_hint = PG_EXCEPTION_HINT;

    RAISE NOTICE 
	'ERROR CODE: % 
	MESSAGE TEXT: % 
	CONTEXT: % 
	DETAIL: % 
	HINT: %', 
   	err_code, msg_text, exc_context, msg_detail, exc_hint;
END;
$BODY$;

-- players
CREATE OR REPLACE PROCEDURE public.import_from_json_players(path_to_file text)
LANGUAGE 'plpgsql'
AS $BODY$
DECLARE
	json_string	jsonb;
	
	err_code text;
	msg_text text;
	exc_context text;
	msg_detail text;
 	exc_hint text;
BEGIN
	json_string := pg_read_file(path_to_file);
	insert into public.players
	select (obj->>'id')::integer as id,  
	(obj->>'name') as name, 
	(obj->>'position') as position, 
	(obj->>'date_of_birth')::date as date_of_birth, 
	(obj->>'nationality') as nationality, 
	(obj->>'team_id')::integer as team_id
	from jsonb_array_elements(json_string) as obj;
	
	RAISE NOTICE 'Success';
	RAISE NOTICE 'Value of path_to_file: %', path_to_file;
	RAISE NOTICE 'Value of json_string: %', json_string;
	
	EXCEPTION WHEN OTHERS  
	THEN
		GET STACKED DIAGNOSTICS
    	err_code = RETURNED_SQLSTATE,
		msg_text = MESSAGE_TEXT,
    	exc_context = PG_CONTEXT,
 		msg_detail = PG_EXCEPTION_DETAIL,
 		exc_hint = PG_EXCEPTION_HINT;

    RAISE NOTICE 
	'ERROR CODE: % 
	MESSAGE TEXT: % 
	CONTEXT: % 
	DETAIL: % 
	HINT: %', 
   	err_code, msg_text, exc_context, msg_detail, exc_hint;
END;
$BODY$;


-- export users to json
CREATE OR REPLACE PROCEDURE public.export_users_to_json(
	)
LANGUAGE 'plpgsql'
AS $BODY$
DECLARE
    user_data JSON;
BEGIN
    SELECT array_to_json(array_agg(row_to_json(users.*)))
    INTO user_data
    FROM public."user" users;
    RAISE NOTICE 'Exported user data to JSON: %', user_data;

END;
$BODY$;

-- Test json procedures
CALL public.export_users_to_json();




delete from public.areas;
delete from public.competitions;
delete from public.teams;
delete from public.players;

select * from public.areas;
select * from public.competitions;
select * from public.teams;
select * from public.players;

delete from public.user_favorite_players;

call public.import_from_json_areas('D:\BSTU\Kurs_3_1\Coursework\scripts\areas.json');
call public.import_from_json_competitions('D:\BSTU\Kurs_3_1\Coursework\scripts\competitions.json');
call public.import_from_json_teams('D:\BSTU\Kurs_3_1\Coursework\scripts\teams.json');
call public.import_from_json_players('D:\BSTU\Kurs_3_1\Coursework\scripts\players.json');