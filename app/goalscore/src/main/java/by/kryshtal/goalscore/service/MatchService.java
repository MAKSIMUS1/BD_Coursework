package by.kryshtal.goalscore.service;

import by.kryshtal.goalscore.dto.MatchDto;
import by.kryshtal.goalscore.dto.MatchIndividualInfoDto;
import by.kryshtal.goalscore.dto.MatchInfoDto;
import by.kryshtal.goalscore.dto.TeamApplicationDto;
import by.kryshtal.goalscore.entity.Match;
import by.kryshtal.goalscore.entity.Team;
import by.kryshtal.goalscore.entity.TeamOnMatch;
import by.kryshtal.goalscore.repository.UnitOfWork;
import by.kryshtal.goalscore.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

@Service
public class MatchService {
    private final UnitOfWork UOW;
    @Autowired
    public MatchService(UnitOfWork UOW) {
        this.UOW = UOW;
    }
    public void createMatch(MatchDto matchDto) throws SQLException, ParseException {
        UOW.getMatchRepository().insert(Mapper.map(matchDto, Match.class));
    }
    public List<Match> getAllMatches() throws SQLException {
        return UOW.getMatchRepository().getAll();
    }
    public List<MatchInfoDto> getAllMatchInfo() throws SQLException {
        return UOW.getMatchRepository().getAllMatchInfo();
    }
    public List<MatchInfoDto> getAllMatchInfoBySearch(String search) throws SQLException {
        return UOW.getMatchRepository().getAllMatchInfoBySearch(search);
    }
    public MatchIndividualInfoDto getMatchInfo(int id) throws SQLException {
        return UOW.getMatchRepository().getMatchInfoById(id);
    }
    public Match getMatchById(int match_id) throws SQLException {
        return UOW.getMatchRepository().getById(match_id);
    }
}