package by.kryshtal.goalscore.repository;

import by.kryshtal.goalscore.entity.Team;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class TeamRepository extends PostgreBase<Team> {
    public TeamRepository(Connection connection) {
        super(connection);
    }
    @Override
    public void insert(Team element) throws SQLException {
        String sql = "{CALL public.insert_team(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (PreparedStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getId());
            statement.setInt(2, element.getArea_id());
            statement.setString(3, element.getName());
            statement.setString(4, element.getShort_name());
            statement.setString(5, element.getTla());
            statement.setString(6, element.getAddress());
            statement.setString(7, element.getWebsite());
            statement.setInt(8, element.getFounded());
            statement.setString(9, element.getClub_colors());

            statement.execute();
        }
    }

    @Override
    public void delete(Team element) throws SQLException {
        String sql = "{CALL public.delete_team(?)}";
        try (PreparedStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getId());

            statement.execute();
        }
    }

    @Override
    public void update(Team element) throws SQLException {
        String sql = "{CALL public.update_team(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (PreparedStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getId());
            statement.setInt(2, element.getArea_id());
            statement.setString(3, element.getName());
            statement.setString(4, element.getShort_name());
            statement.setString(5, element.getTla());
            statement.setString(6, element.getAddress());
            statement.setString(7, element.getWebsite());
            statement.setInt(8, element.getFounded());
            statement.setString(9, element.getClub_colors());

            statement.execute();
        }
    }

    @Override
    public Team get(Team element) throws SQLException {
        return null;
    }

    @Override
    public Team getById(int id) throws SQLException {
        String query = "select * from public.get_team_by_id(?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Team(
                            resultSet.getInt("id"),
                            resultSet.getInt("area_id"),
                            resultSet.getString("name"),
                            resultSet.getString("short_name"),
                            resultSet.getString("tla"),
                            resultSet.getString("address"),
                            resultSet.getString("website"),
                            resultSet.getInt("founded"),
                            resultSet.getString("club_colors"));
                }
                else{
                    return null;
                }
            }
        }
    }

    @Override
    public List<Team> getAll() throws SQLException {
        List<Team> teams = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement("select * from teams");
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                teams.add(new Team(
                        rs.getInt("id"),
                        rs.getInt("area_id"),
                        rs.getString("name"),
                        rs.getString("short_name"),
                        rs.getString("tla"),
                        rs.getString("address"),
                        rs.getString("website"),
                        rs.getInt("founded"),
                        rs.getString("club_colors")
                        ));
            }
        return teams;
    }
}
