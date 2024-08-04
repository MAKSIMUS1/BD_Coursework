CREATE OR REPLACE VIEW public.matches_info_view AS
SELECT
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
