package by.kryshtal.goalscore.repository;

import by.kryshtal.goalscore.entity.Team;
import by.kryshtal.goalscore.entity.User;
import by.kryshtal.goalscore.security.Hasher;

import javax.validation.constraints.Null;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository extends PostgreBase<User> {
    public UserRepository(Connection connection) {
        super(connection);
    }
    @Override
    public void insert(User element) throws SQLException {
        String sql = "{? = call public.register_user(?, ?, ?)}";
        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, element.getUsername());
            callableStatement.setString(3, Hasher.getHash(element.getPassword()));
            callableStatement.setString(4, element.getPhone());

            callableStatement.executeUpdate();
        }
    }

    @Override
    public void delete(User  element) throws SQLException {

    }

    @Override
    public void update(User  element) throws SQLException {

    }

    @Override
    public User get(User  element) throws SQLException {
        return null;
    }

    @Override
    public User getById(int id) throws SQLException {
        String sql = "select * from public.get_user_by_id(?)";
        try (PreparedStatement preparedStatement = connection.prepareCall(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getInt("favorite_team_id"),
                        resultSet.getBytes("profile_image"),
                        resultSet.getString("phone"),
                        resultSet.getDate("join_date"),
                        resultSet.getString("role")
                );
            } else {
                return null;
            }
        }
    }

    @Override
    public List<User > getAll() throws SQLException {
        return null;
    }
    public User findByLogin(String login) throws  SQLException{
        List<User> users = new ArrayList<>();

        String sql = "select * from public.find_user_by_login(?)";
        try  {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, login);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("favorite_team_id"),
                        rs.getBytes("profile_image"),
                        rs.getString("phone"),
                        rs.getDate("join_date"),
                        rs.getString("role")
                ));
            }
        }
        catch (Throwable t)
        {
            if(!t.getMessage().contains("no user with such login"))
                throw t;
            return null;
        }
        return users.get(0);
    }
    public String getUserRole(String token)throws SQLException {
        String sql = "SELECT get_user_role_by_token(?) AS role";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, token);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("role");
                }
            }
        }
        return null;
    }
    public void setProfile_image(int _user_id, byte[] _bytes) throws SQLException {
        String sql = "{call public.update_user_profile_image(?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, _user_id);
            statement.setBytes(2, _bytes);

            statement.execute();
        }
    }
}
