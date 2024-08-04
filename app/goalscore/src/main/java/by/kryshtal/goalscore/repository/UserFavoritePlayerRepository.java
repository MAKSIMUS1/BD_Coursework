package by.kryshtal.goalscore.repository;

import by.kryshtal.goalscore.dto.PlayerInfoDto;
import by.kryshtal.goalscore.entity.Player;
import by.kryshtal.goalscore.entity.User;
import by.kryshtal.goalscore.entity.UserFavoritePlayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserFavoritePlayerRepository extends PostgreBase<UserFavoritePlayer> {
    public UserFavoritePlayerRepository(Connection connection) {
        super(connection);
    }
    @Override
    public void insert(UserFavoritePlayer element) throws SQLException {
        String sql = "{call public.insert_user_favorite_player(?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getUser_id());
            statement.setInt(2, element.getPlayer_id());

            statement.execute();
        }
    }

    @Override
    public void delete(UserFavoritePlayer element) throws SQLException {
        String sql = "{call public.remove_favorite_player(?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getUser_id());
            statement.setInt(2, element.getPlayer_id());

            statement.execute();
        }
    }

    @Override
    public void update(UserFavoritePlayer element) throws SQLException {
    }

    @Override
    public UserFavoritePlayer get(UserFavoritePlayer element) throws SQLException {
        return null;
    }

    @Override
    public UserFavoritePlayer getById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<UserFavoritePlayer> getAll() throws SQLException {
        return null;
    }
    public List<PlayerInfoDto> getFavouritePlayersData(int user_id) throws SQLException {
        List<PlayerInfoDto> players = new ArrayList<>();
        String sql = "select * from public.get_favorite_players_data(?)";
        try  {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user_id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                players.add(new PlayerInfoDto(
                        rs.getInt("player_id"),
                        rs.getString("player_name"),
                        rs.getString("player_position"),
                        rs.getDate("player_date_of_birth"),
                        rs.getString("player_nationality"),
                        rs.getString("team_short_name"),
                        rs.getInt("team_id")
                ));
            }
            return players;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
}

