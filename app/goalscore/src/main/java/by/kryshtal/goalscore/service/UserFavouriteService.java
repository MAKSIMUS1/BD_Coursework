package by.kryshtal.goalscore.service;

import by.kryshtal.goalscore.dto.PlayerInfoDto;
import by.kryshtal.goalscore.entity.UserFavoritePlayer;
import by.kryshtal.goalscore.entity.UserFavoriteTeam;
import by.kryshtal.goalscore.entity.UserStarTeam;
import by.kryshtal.goalscore.repository.UnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.List;

@Service
public class UserFavouriteService {
    private final UnitOfWork UOW;
    @Autowired
    public UserFavouriteService(UnitOfWork UOW) {
        this.UOW = UOW;
    }
    public void addFavouritePlayer(UserFavoritePlayer _user_player) throws SQLException {
        UOW.getUserFavoritePlayerRepository().insert(_user_player);
    }
    public List<PlayerInfoDto> getFavouritePlayers(int _user_id) throws SQLException {
        return UOW.getUserFavoritePlayerRepository().getFavouritePlayersData(_user_id);
    }
    public void removeFavoritePlayer(int _user_id, int player_id) throws SQLException {
        UserFavoritePlayer user_favourite_player = new UserFavoritePlayer();
        user_favourite_player.setUser_id(_user_id);
        user_favourite_player.setPlayer_id(player_id);
        UOW.getUserFavoritePlayerRepository().delete(user_favourite_player);
    }
    public void addFavouriteTeam(UserFavoriteTeam _user_team) throws SQLException {
        UOW.getUserFavoriteTeamRepository().insert(_user_team);
    }
    public List<UserFavoriteTeam> getFavouriteTeams(int _user_id) throws SQLException {
        return UOW.getUserFavoriteTeamRepository().getFavouriteTeams(_user_id);
    }
    public void removeFavoriteTeam(int _user_id, int team_id) throws SQLException {
        UserFavoriteTeam user_favourite_team = new UserFavoriteTeam();
        user_favourite_team.setUser_id(_user_id);
        user_favourite_team.setTeam_id(team_id);
        UOW.getUserFavoriteTeamRepository().delete(user_favourite_team);
    }

    public UserStarTeam getUserStarTeam(int _user_id) throws SQLException {
        return UOW.getUserStarTeamRepository().getByUserId(_user_id);
    }
    public void buildUserTeam(int _user_id,
                              int _goalkeeper_id,
                              int _leftDefender_id,
                              int _central1Defender_id,
                              int _central2Defender_id,
                              int _rightDefender_id,
                              int _leftMidfielder_id,
                              int _centralMidfielder_id,
                              int _rightMidfielder_id,
                              int _leftWinger_id,
                              int _striker_id,
                              int _rightWinger_id) throws SQLException {
        UserStarTeam user_team = new UserStarTeam();
        user_team.setUser_id(_user_id);

        user_team.setGoalkeeper_id(_goalkeeper_id);

        user_team.setLeft_defender_id(_leftDefender_id);
        user_team.setCentral_1_defender_id(_central1Defender_id);
        user_team.setCentral_2_defender_id(_central2Defender_id);
        user_team.setRight_defender_id(_rightDefender_id);

        user_team.setLeft_midfielder_id(_leftMidfielder_id);
        user_team.setCentral_midfielder_id(_centralMidfielder_id);
        user_team.setRight_midfielder_id(_rightMidfielder_id);

        user_team.setLeft_winger_id(_leftWinger_id);
        user_team.setStriker_id(_striker_id);
        user_team.setRight_winger_id(_rightWinger_id);

        UOW.getUserStarTeamRepository().saveTeam(user_team);
    }
}
