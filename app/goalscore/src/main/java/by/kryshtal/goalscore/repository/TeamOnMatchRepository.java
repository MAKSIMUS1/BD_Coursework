package by.kryshtal.goalscore.repository;

import by.kryshtal.goalscore.entity.Team;
import by.kryshtal.goalscore.entity.TeamOnMatch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeamOnMatchRepository extends PostgreBase<TeamOnMatch> {

    public TeamOnMatchRepository(Connection connection) {
        super(connection);
    }
    @Override
    public void insert(TeamOnMatch element) throws SQLException {
        String sql = "{CALL public.insert_new_team_on_match(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (PreparedStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getTeam_id());
            statement.setInt(2, element.getGoalkeeper_id());
            statement.setInt(3, element.getLeft_defender_id());
            statement.setInt(4, element.getCentral_1_defender_id());
            statement.setInt(5, element.getCentral_2_defender_id());
            statement.setInt(6, element.getRight_defender_id());
            statement.setInt(7, element.getLeft_midfielder_id());
            statement.setInt(8, element.getCentral_midfielder_id());
            statement.setInt(9, element.getRight_midfielder_id());
            statement.setInt(10, element.getLeft_winger_id());
            statement.setInt(11, element.getStriker_id());
            statement.setInt(12, element.getRight_winger_id());
            statement.setString(13, element.getComment());

            statement.execute();
        }
    }

    @Override
    public void delete(TeamOnMatch element) throws SQLException {

    }

    @Override
    public void update(TeamOnMatch element) throws SQLException {

    }

    @Override
    public TeamOnMatch get(TeamOnMatch element) throws SQLException {
        return null;
    }

    @Override
    public TeamOnMatch getById(int id) throws SQLException {
        String query = "select * from public.get_team_on_match_by_id(?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new TeamOnMatch(
                            resultSet.getInt("id"),
                            resultSet.getInt("team_id"),
                            resultSet.getInt("goalkeeper_id"),
                            resultSet.getInt("left_defender_id"),
                            resultSet.getInt("central_1_defender_id"),
                            resultSet.getInt("central_2_defender_id"),
                            resultSet.getInt("right_defender_id"),
                            resultSet.getInt("left_midfielder_id"),
                            resultSet.getInt("central_midfielder_id"),
                            resultSet.getInt("right_midfielder_id"),
                            resultSet.getInt("left_winger_id"),
                            resultSet.getInt("striker_id"),
                            resultSet.getInt("right_winger_id"),
                            resultSet.getString("comment"));
                }
                else{
                    return null;
                }
            }
        }
    }

    @Override
    public List<TeamOnMatch> getAll() throws SQLException {
        return null;
    }
}
