package by.kryshtal.goalscore.repository;

import by.kryshtal.goalscore.dto.PlayerInfoDto;
import by.kryshtal.goalscore.dto.PlayerMatchDto;
import by.kryshtal.goalscore.entity.Player;
import by.kryshtal.goalscore.entity.Team;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class PlayerRepository extends PostgreBase<Player>{


    public PlayerRepository(Connection connection) {
        super(connection);
    }
    @Override
    public void insert(Player element) throws SQLException, ParseException {
        String sql = "{CALL public.insert_player(?, ?, ?, ?, ?, ?)}";
        try (PreparedStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getId());
            statement.setString(2, element.getName());
            statement.setString(3, element.getPosition());
            statement.setDate(4, new Date(element.getDate_of_birth().getTime()));
            statement.setString(5, element.getNationality());
            statement.setInt(6, element.getTeam_id());

            statement.execute();
        }
    }

    @Override
    public void delete(Player element) throws SQLException {
        String sql = "{CALL public.delete_player(?)}";
        try (PreparedStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getId());

            statement.execute();
        }
    }

    @Override
    public void update(Player element) throws SQLException {
        String sql = "{CALL public.update_player(?, ?, ?, ?, ?, ?)}";
        try (PreparedStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getId());
            statement.setString(2, element.getName());
            statement.setString(3, element.getPosition());
            statement.setDate(4, new Date(element.getDate_of_birth().getTime()));
            statement.setString(5, element.getNationality());
            statement.setInt(6, element.getTeam_id());

            statement.execute();
        }
    }

    @Override
    public Player get(Player element) throws SQLException {
        return null;
    }

    @Override
    public Player getById(int id) throws SQLException {
        String query = "select * from public.get_player_by_id(?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Player(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("player_position"),
                            resultSet.getDate("date_of_birth"),
                            resultSet.getString("nationality"),
                            resultSet.getInt("team_id"));
                }
                else{
                    return null;
                }
            }
        }
    }

    @Override
    public List<Player> getAll() throws SQLException {
        return null;
    }

    public List<PlayerMatchDto> getPlayersLineupByTemOnMatchId(int teamOnMatchId) throws SQLException {
        List<PlayerMatchDto> players = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from public.get_starting_players( ? )")) {
            preparedStatement.setInt(1, teamOnMatchId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    players.add(new PlayerMatchDto(rs.getInt("player_id"),
                            rs.getString("player_name"),
                            rs.getString("player_position")));
                }
            }
        }
        return players;
    }

    public List<PlayerInfoDto> getPlayersInRange(int start_range) throws SQLException {
        List<PlayerInfoDto> players = new ArrayList<>();
        String sql = "select * from public.get_players_range(?)";
        try  {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, start_range);
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
    public int getPlayersPages() throws SQLException {
        String sql = "{ ? = call public.get_total_player_pages()}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, Types.INTEGER);
            statement.execute();
            return statement.getInt(1);
        }
    }

}
