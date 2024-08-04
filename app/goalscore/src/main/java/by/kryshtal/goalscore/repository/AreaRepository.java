package by.kryshtal.goalscore.repository;


import by.kryshtal.goalscore.entity.Area;
import by.kryshtal.goalscore.entity.Team;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AreaRepository extends PostgreBase<Area> {
    public AreaRepository(Connection connection) {
        super(connection);
    }
    @Override
    public void insert(Area element) throws SQLException {

    }

    @Override
    public void delete(Area element) throws SQLException {

    }

    @Override
    public void update(Area element) throws SQLException {

    }

    @Override
    public Area get(Area element) throws SQLException {
        return null;
    }

    @Override
    public Area getById(int id) throws SQLException {
        String query = "select * from public.get_area_by_id(?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Area(
                            resultSet.getInt("area_id"),
                            resultSet.getString("area_name"));
                }
                else{
                    return null;
                }
            }
        }
    }

    @Override
    public List<Area> getAll() throws SQLException {
        return null;
    }
}
