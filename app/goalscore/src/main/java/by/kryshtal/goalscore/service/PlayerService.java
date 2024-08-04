package by.kryshtal.goalscore.service;

import by.kryshtal.goalscore.dto.PlayerInfoDto;
import by.kryshtal.goalscore.dto.PlayerMatchDto;
import by.kryshtal.goalscore.entity.Player;
import by.kryshtal.goalscore.entity.Team;
import by.kryshtal.goalscore.repository.UnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

@Service
public class PlayerService {
    private final UnitOfWork UOW;
    @Autowired
    public PlayerService(UnitOfWork UOW) {
        this.UOW = UOW;
    }

    public List<PlayerMatchDto> getLineupInfo(int team_on_match_id) throws SQLException {
        return UOW.getPlayerRepository().getPlayersLineupByTemOnMatchId(team_on_match_id);
    }
    public List<PlayerInfoDto> getPlayersInRange(int start_index) throws SQLException {
        return UOW.getPlayerRepository().getPlayersInRange(start_index);
    }
    public int getPlayersPages() throws SQLException {
        return UOW.getPlayerRepository().getPlayersPages();
    }
    public void createPlayer(Player player) throws SQLException, ParseException {
        UOW.getPlayerRepository().insert(player);
    }
    public void updatePlayer(Player player) throws SQLException{
        UOW.getPlayerRepository().update(player);
    }
    public void removePlayer(Player player) throws SQLException{
        UOW.getPlayerRepository().delete(player);
    }
    public Player getPlayerById(int player_id)throws SQLException{
        return UOW.getPlayerRepository().getById(player_id);
    }
}
