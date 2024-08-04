package by.kryshtal.goalscore.service;

import by.kryshtal.goalscore.dto.TeamApplicationDto;
import by.kryshtal.goalscore.entity.TeamOnMatch;
import by.kryshtal.goalscore.repository.UnitOfWork;
import by.kryshtal.goalscore.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class TeamApplicationService {
    private final UnitOfWork UOW;
    @Autowired
    public TeamApplicationService(UnitOfWork UOW) {
        this.UOW = UOW;
    }
    public void createTeamApplication(TeamApplicationDto teamAppldto) throws SQLException {
        UOW.getTeamOnMatchRepository().insert(Mapper.map(teamAppldto, TeamOnMatch.class));
    }
    public TeamOnMatch getTeamOnMatchById(int team_on_match_id) throws SQLException {
        return UOW.getTeamOnMatchRepository().getById(team_on_match_id);
    }
}