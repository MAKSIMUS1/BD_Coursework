package by.kryshtal.goalscore.repository;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Component
public final class UnitOfWork {

    private static final Connection _connection;

    private static final TeamRepository _teamRepository;
    private static final TokenRepository _tokenRepository;
    private static final TeamOnMatchRepository _teamOnMatchRepository;
    private static final MatchRepository _matchRepository;
    private static final PlayerRepository _playerRepository;
    private static final UserRepository _userRepository;
    private static final UserFavoritePlayerRepository _userFavoritePlayerRepository;
    private static final UserFavoriteTeamRepository _userFavoriteTeamRepository;
    private static final UserStarTeamRepository _userStarTeamRepository;
    private static final AreaRepository _areaRepository;
    static
    {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            Properties props = new Properties();
            props.setProperty("escapeSyntaxCallMode", "callIfNoReturn");
            props.put("user", "postgres");
            props.put("password", "1234");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/goalscore", props);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
        _connection = c;
        try {
            _connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        _teamRepository = new TeamRepository(c);
        _userRepository = new UserRepository(c);
        _tokenRepository = new TokenRepository(c);
        _teamOnMatchRepository = new TeamOnMatchRepository(c);
        _matchRepository = new MatchRepository(c);
        _playerRepository = new PlayerRepository(c);
        _userFavoritePlayerRepository = new UserFavoritePlayerRepository(c);
        _userFavoriteTeamRepository = new UserFavoriteTeamRepository(c);
        _userStarTeamRepository = new UserStarTeamRepository(c);
        _areaRepository = new AreaRepository(c);
    }

    public TeamRepository getTeamRepository() {
        return _teamRepository;
    }
    public UserRepository getUserRepository() {
        return _userRepository;
    }
    public TokenRepository getTokenRepository() {
        return _tokenRepository;
    }
    public TeamOnMatchRepository getTeamOnMatchRepository() {
        return _teamOnMatchRepository;
    }
    public MatchRepository getMatchRepository() {
        return _matchRepository;
    }
    public PlayerRepository getPlayerRepository() {
        return _playerRepository;
    }
    public UserFavoritePlayerRepository getUserFavoritePlayerRepository() {return _userFavoritePlayerRepository;}
    public UserFavoriteTeamRepository getUserFavoriteTeamRepository() {return _userFavoriteTeamRepository;}
    public UserStarTeamRepository getUserStarTeamRepository() {return _userStarTeamRepository;}
    public AreaRepository getAreaRepository() {return _areaRepository;}
}
