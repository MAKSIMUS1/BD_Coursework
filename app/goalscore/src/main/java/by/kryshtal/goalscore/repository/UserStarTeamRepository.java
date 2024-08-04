package by.kryshtal.goalscore.repository;

import by.kryshtal.goalscore.entity.Match;
import by.kryshtal.goalscore.entity.User;
import by.kryshtal.goalscore.entity.UserStarTeam;

import java.sql.*;
import java.util.List;

public class UserStarTeamRepository extends PostgreBase<UserStarTeam> {
    public UserStarTeamRepository(Connection connection) {
        super(connection);
    }

    @Override
    public void insert(UserStarTeam element) throws SQLException {
        String sql = "{call public.insert_user_star_team(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getUser_id());
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

            statement.execute();
        }
    }

    @Override
    public void delete(UserStarTeam element) throws SQLException {

    }

    @Override
    public void update(UserStarTeam element) throws SQLException {
        String sql = "{call public.update_user_star_team(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getUser_id());
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

            statement.execute();
        }
    }

    @Override
    public UserStarTeam get(UserStarTeam element) throws SQLException {
        return null;
    }

    @Override
    public UserStarTeam getById(int id) throws SQLException {
        return null;
    }
    public UserStarTeam getByUserId(int user_id) throws SQLException {
        String query = "select * from public.get_user_star_team(?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, user_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    UserStarTeam star_team = new UserStarTeam();
                    star_team.setGoalkeeper_id(resultSet.getInt("goalkeeper_id"));

                    star_team.setLeft_defender_id(resultSet.getInt("left_defender_id"));
                    star_team.setCentral_1_defender_id(resultSet.getInt("central_1_defender_id"));
                    star_team.setCentral_2_defender_id(resultSet.getInt("central_2_defender_id"));
                    star_team.setRight_defender_id(resultSet.getInt("right_defender_id"));

                    star_team.setLeft_midfielder_id(resultSet.getInt("left_midfielder_id"));
                    star_team.setCentral_midfielder_id(resultSet.getInt("central_midfielder_id"));
                    star_team.setRight_midfielder_id(resultSet.getInt("right_midfielder_id"));

                    star_team.setLeft_winger_id(resultSet.getInt("left_winger_id"));
                    star_team.setStriker_id(resultSet.getInt("striker_id"));
                    star_team.setRight_winger_id(resultSet.getInt("right_winger_id"));
                    return star_team;
                }
                else{
                    return null;
                }
            }
        }
    }

    @Override
    public List<UserStarTeam> getAll() throws SQLException {
        return null;
    }
    public void saveTeam(UserStarTeam element) throws SQLException {
        String sql = "{ ? = call public.check_user_star_team_exists(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, Types.BOOLEAN);
            statement.setInt(2, element.getUser_id());

            statement.execute();

            boolean result = statement.getBoolean(1);

            if(result){
                update(element);
            }else{
                insert(element);
            }
        }
    }
}
