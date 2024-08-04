package by.kryshtal.goalscore.repository;

import by.kryshtal.goalscore.dto.MatchIndividualInfoDto;
import by.kryshtal.goalscore.dto.MatchInfoDto;
import by.kryshtal.goalscore.entity.Match;
import by.kryshtal.goalscore.entity.Team;
import by.kryshtal.goalscore.entity.TeamOnMatch;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MatchRepository extends PostgreBase<Match> {

    public MatchRepository(Connection connection) {
        super(connection);
    }
    @Override
    public void insert(Match element) throws SQLException, ParseException {
        String sql = "{CALL public.insert_match(?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement callableStatement = connection.prepareCall(sql)) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date parsedDate = dateFormat.parse(element.getMatch_date());
            Timestamp timestamp = new Timestamp(parsedDate.getTime());

            callableStatement.setInt(1, element.getCompetition_id());
            callableStatement.setInt(2, element.getAttendance());
            callableStatement.setInt(3, element.getHome_match_team_id());
            callableStatement.setInt(4, element.getAway_match_team_id());
            callableStatement.setObject(5, element.getHome_goal_score(), Types.SMALLINT);
            callableStatement.setObject(6, element.getAway_goal_score(), Types.SMALLINT);
            callableStatement.setObject(7, timestamp, Types.TIMESTAMP);

            callableStatement.execute();
        }
        catch (ParseException e) {
            throw e;
        }
    }

    @Override
    public void delete(Match element) throws SQLException {

    }

    @Override
    public void update(Match element) throws SQLException {

    }

    @Override
    public Match get(Match element) throws SQLException {
        return null;
    }

    @Override
    public Match getById(int id) throws SQLException {
        String query = "select * from public.get_match_by_id(?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Match(
                            resultSet.getInt("match_id"),
                            resultSet.getInt("competition_id"),
                            resultSet.getInt("attendance"),
                            resultSet.getInt("home_match_team_id"),
                            resultSet.getInt("away_match_team_id"),
                            resultSet.getShort("home_goal_score"),
                            resultSet.getShort("away_goal_score"),
                            resultSet.getString("match_datetime"));
                }
                else{
                    return null;
                }
            }
        }
    }

    @Override
    public List<Match> getAll() throws SQLException {
        List<Match> matches = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM get_all_matches()")) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int matchId = resultSet.getInt("match_id");
                int competitionId = resultSet.getInt("competition_id");
                int attendance = resultSet.getInt("attendance");
                int homeMatchTeamId = resultSet.getInt("home_match_team_id");
                int awayMatchTeamId = resultSet.getInt("away_match_team_id");
                short homeGoalScore = resultSet.getShort("home_goal_score");
                short awayGoalScore = resultSet.getShort("away_goal_score");
                Timestamp matchDatetime = resultSet.getTimestamp("match_datetime");

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = dateFormat.format(matchDatetime);

                matches.add(new Match(matchId, competitionId, attendance,
                        homeMatchTeamId, awayMatchTeamId, homeGoalScore, awayGoalScore, formattedDate));
            }
        }
        return matches;
    }

    public List<MatchInfoDto> getAllMatchInfo() throws SQLException {
        List<MatchInfoDto> matchInfoList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from public.get_matches_info()")) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    MatchInfoDto matchInfo = new MatchInfoDto();
                    matchInfo.setMatchId(rs.getInt("match_id"));
                    matchInfo.setCompetitionName(rs.getString("competition_name"));
                    matchInfo.setAreaName(rs.getString("area_name"));
                    matchInfo.setMatchDatetime(rs.getTimestamp("match_datetime"));
                    matchInfo.setHomeTeamName(rs.getString("home_team_name"));
                    matchInfo.setHomeTeamId(rs.getInt("home_team_id"));
                    matchInfo.setAwayTeamName(rs.getString("away_team_name"));
                    matchInfo.setAwayTeamId(rs.getInt("away_team_id"));
                    matchInfo.setHomeGoal(rs.getShort("home_goal"));
                    matchInfo.setAwayGoal(rs.getShort("away_goal"));

                    matchInfoList.add(matchInfo);
                }
            }
        }
        return matchInfoList;
    }
    public List<MatchInfoDto> getAllMatchInfoBySearch(String search_query) throws SQLException {
        List<MatchInfoDto> matchInfoList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from public.get_searched_matches_info(?)")) {
            preparedStatement.setString(1,search_query);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    MatchInfoDto matchInfo = new MatchInfoDto();
                    matchInfo.setMatchId(rs.getInt("match_id"));
                    matchInfo.setCompetitionName(rs.getString("competition_name"));
                    matchInfo.setAreaName(rs.getString("area_name"));
                    matchInfo.setMatchDatetime(rs.getTimestamp("match_datetime"));
                    matchInfo.setHomeTeamName(rs.getString("home_team_name"));
                    matchInfo.setHomeTeamId(rs.getInt("home_team_id"));
                    matchInfo.setAwayTeamName(rs.getString("away_team_name"));
                    matchInfo.setAwayTeamId(rs.getInt("away_team_id"));
                    matchInfo.setHomeGoal(rs.getShort("home_goal"));
                    matchInfo.setAwayGoal(rs.getShort("away_goal"));

                    matchInfoList.add(matchInfo);
                }
            }
        }
        return matchInfoList;
    }
    public MatchIndividualInfoDto getMatchInfoById(int match_id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from public.get_match_info_by_id( ? )")) {
            preparedStatement.setInt(1, match_id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    MatchIndividualInfoDto matchInfo = new MatchIndividualInfoDto();
                    matchInfo.setMatchId(rs.getInt("match_id"));
                    matchInfo.setCompetitionName(rs.getString("competition_name"));
                    matchInfo.setAreaName(rs.getString("area_name"));
                    matchInfo.setMatchDatetime(rs.getTimestamp("match_datetime"));
                    matchInfo.setHomeTeamName(rs.getString("home_team_name"));
                    matchInfo.setHomeTeamId(rs.getInt("home_team_id"));
                    matchInfo.setAwayTeamName(rs.getString("away_team_name"));
                    matchInfo.setAwayTeamId(rs.getInt("away_team_id"));
                    matchInfo.setHomeGoal(rs.getShort("home_goal"));
                    matchInfo.setAwayGoal(rs.getShort("away_goal"));
                    matchInfo.setAttendance(rs.getInt("attendance"));

                    return matchInfo;
                }else{
                    return null;
                }
            }
        }
    }
}