package by.kryshtal.goalscore.repository;

import by.kryshtal.goalscore.dto.PlayerInfoDto;
import by.kryshtal.goalscore.entity.UserFavoriteTeam;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserFavoriteTeamRepository extends PostgreBase<UserFavoriteTeam> {
    public UserFavoriteTeamRepository(Connection connection) {
        super(connection);
    }
    @Override
    public void insert(UserFavoriteTeam element) throws SQLException {
        String sql = "{call public.insert_user_favorite_team(?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getUser_id());
            statement.setInt(2, element.getTeam_id());

            statement.execute();
        }
    }

    @Override
    public void delete(UserFavoriteTeam element) throws SQLException {
        String sql = "{call public.remove_favorite_team(?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getUser_id());
            statement.setInt(2, element.getTeam_id());

            statement.execute();
        }
    }

    @Override
    public void update(UserFavoriteTeam element) throws SQLException {
    }

    @Override
    public UserFavoriteTeam get(UserFavoriteTeam element) throws SQLException {
        return null;
    }

    @Override
    public UserFavoriteTeam getById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<UserFavoriteTeam> getAll() throws SQLException {
        return null;
    }
    public List<UserFavoriteTeam> getFavouriteTeams(int user_id) throws SQLException {
        List<UserFavoriteTeam> teams = new ArrayList<>();
        String sql = "select * from public.get_favorite_teams_by_user_id(?)";
        try  {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user_id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                teams.add(new UserFavoriteTeam(
                        rs.getInt("favorite_team_id"),
                        rs.getInt("user_id"),
                        rs.getInt("team_id")
                ));
            }
            return teams;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
}