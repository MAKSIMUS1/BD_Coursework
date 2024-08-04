package by.kryshtal.goalscore.service;

import by.kryshtal.goalscore.entity.Team;
import by.kryshtal.goalscore.repository.UnitOfWork;
import org.hibernate.sql.exec.SqlExecLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class TeamService {
    private final UnitOfWork UOW;
    @Autowired
    public TeamService(UnitOfWork UOW) {
        this.UOW = UOW;
    }
    public List<Team> getAllTeams() throws SQLException {
        return UOW.getTeamRepository().getAll();
    }
    public Team getTeamById(int id) throws SQLException {
        return UOW.getTeamRepository().getById(id);
    }
    public void createTeam(Team team) throws SQLException{
        UOW.getTeamRepository().insert(team);
    }
    public void updateTeam(Team team) throws SQLException{
        UOW.getTeamRepository().update(team);
    }
    public void removeTeam(Team team) throws SQLException{
        UOW.getTeamRepository().delete(team);
    }

}
