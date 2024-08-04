CREATE USER replicator REPLICATION LOGIN CONNECTION LIMIT 10 ENCRYPTED PASSWORD '1234';

create role ROLE_UNAUTHORIZED;
grant connect on database goalscore to ROLE_UNAUTHORIZED;

grant SELECT on public.user to ROLE_UNAUTHORIZED;
grant INSERT on public.user to ROLE_UNAUTHORIZED;
grant INSERT on token to ROLE_UNAUTHORIZED;
grant select on match to ROLE_UNAUTHORIZED;
grant execute on FUNCTION get_searched_matches_info to ROLE_UNAUTHORIZED;
grant execute on procedure change_token to ROLE_UNAUTHORIZED;
grant execute on procedure insert_new_user to ROLE_UNAUTHORIZED;
grant execute on FUNCTION register_user to ROLE_UNAUTHORIZED;
grant execute on FUNCTION is_token_with_user_id to ROLE_UNAUTHORIZED;

create role ROLE_USER;
grant ROLE_UNAUTHORIZED to ROLE_USER;
grant insert on user_favorite_players to ROLE_USER;
grant update on user_favorite_players to ROLE_USER;
grant delete on user_favorite_players to ROLE_USER;
grant insert on user_favorite_teams to ROLE_USER;
grant update on user_favorite_teams to ROLE_USER;
grant delete on user_favorite_teams to ROLE_USER;
grant insert on user_star_team to ROLE_USER;
grant update on user_star_team to ROLE_USER;
grant delete on user_star_team to ROLE_USER;
grant select on teams to ROLE_USER;
grant select on players to ROLE_USER;

create role ROLE_ADMIN;
grant ROLE_USER to ROLE_ADMIN;
grant insert on match to ROLE_ADMIN;
grant update on match to ROLE_ADMIN;
grant delete on match to ROLE_ADMIN;
grant insert on players to ROLE_ADMIN;
grant update on players to ROLE_ADMIN;
grant delete on players to ROLE_ADMIN;
grant insert on team_on_match to ROLE_ADMIN;
grant update on team_on_match to ROLE_ADMIN;
grant delete on team_on_match to ROLE_ADMIN;
grant insert on teams to ROLE_ADMIN;
grant update on teams to ROLE_ADMIN;
grant delete on teams to ROLE_ADMIN;
grant insert on areas to ROLE_ADMIN;
grant delete on areas to ROLE_ADMIN;
grant insert on competitions to ROLE_ADMIN;
grant delete on competitions to ROLE_ADMIN;