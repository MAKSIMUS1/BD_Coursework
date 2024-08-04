CREATE OR REPLACE FUNCTION check_teams_different()
RETURNS TRIGGER AS $$
DECLARE
    home_team_id integer;
    away_team_id integer;
BEGIN
    SELECT team_id INTO home_team_id
    FROM public.team_on_match
    WHERE id = NEW.home_match_team_id;

    SELECT team_id INTO away_team_id
    FROM public.team_on_match
    WHERE id = NEW.away_match_team_id;

    IF home_team_id = away_team_id THEN
        RAISE EXCEPTION 'Home team and away team must be different';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_teams_different_trigger
BEFORE INSERT OR UPDATE ON public.match
FOR EACH ROW
EXECUTE FUNCTION check_teams_different();