package by.kryshtal.goalscore.repository;

import by.kryshtal.goalscore.entity.Token;

import java.sql.*;
import java.util.List;

public class TokenRepository extends PostgreBase<Token> {
    public TokenRepository(Connection connection) {
        super(connection);
    }
    @Override
    public void insert(Token element) throws SQLException {
        String sql = "{call public.insert_new_token(?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getUser_id());
            statement.setString(2, element.getToken());

            statement.execute();
        }
    }

    @Override
    public void delete(Token element) throws SQLException {

    }

    @Override
    public void update(Token element) throws SQLException {
        String sql = "{call public.change_token(?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, element.getUser_id());
            statement.setString(2, element.getToken());

            statement.execute();
        }
    }

    @Override
    public Token get(Token element) throws SQLException {
        return null;
    }

    @Override
    public Token getById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Token> getAll() throws SQLException {
        return null;
    }
    public void save(Token token) throws SQLException{
        String sql = "{ ? = call public.is_token_with_user_id(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, Types.BOOLEAN);
            statement.setInt(2, token.getUser_id());

            statement.execute();

            boolean result = statement.getBoolean(1);

            if(result){
                update(token);
            }else{
                insert(token);
            }
        }
    }
    public int getUserIdByToken(String _token) throws SQLException{
        String sql = "{ ? = call public.get_user_id_by_token(?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.registerOutParameter(1, Types.INTEGER);
            statement.setString(2, _token);

            statement.execute();

            int result = statement.getInt(1);
            return result;
        }
    }
}
