package by.kryshtal.goalscore.controller;

import by.kryshtal.goalscore.dto.PlayerMatchDto;
import by.kryshtal.goalscore.entity.Match;
import by.kryshtal.goalscore.service.MatchService;
import by.kryshtal.goalscore.service.PlayerService;
import by.kryshtal.goalscore.service.TeamApplicationService;
import by.kryshtal.goalscore.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Controller
public class MatchController {
    private final MatchService matchService;
    private final PlayerService playerService;
    private final TeamService teamService;
    private final TeamApplicationService teamApplicationService;
    @Autowired
    public MatchController(MatchService matchService, PlayerService playerService, TeamService teamService, TeamApplicationService teamApplicationService) {
        this.matchService = matchService;
        this.playerService = playerService;
        this.teamService = teamService;
        this.teamApplicationService = teamApplicationService;
    }
    @GetMapping("/matches/{id}")
    public String getMatchDetails(@PathVariable("id") int matchId, Model model) {
        try{
            Match match =  matchService.getMatchById(matchId);
            model.addAttribute("match_date", match.getMatch_date());
            model.addAttribute("competition_name", match.getCompetition_id());

            int homeTeamId = teamApplicationService.getTeamOnMatchById(match.getHome_match_team_id()).getTeam_id();
            int awayTeamId = teamApplicationService.getTeamOnMatchById(match.getAway_match_team_id()).getTeam_id();
            model.addAttribute("home_team_name", teamService.getTeamById(homeTeamId).getName());
            model.addAttribute("away_team_name", teamService.getTeamById(awayTeamId).getName());

            model.addAttribute("home_team_id", homeTeamId);
            model.addAttribute("away_team_id", awayTeamId);

            List<PlayerMatchDto> hpl = playerService.getLineupInfo(match.getHome_match_team_id());
            List<PlayerMatchDto> apl = playerService.getLineupInfo(match.getAway_match_team_id());
            List<PlayerMatchDto> home_start_players = new ArrayList<>();
            List<PlayerMatchDto> home_bench_players = new ArrayList<>();
            List<PlayerMatchDto> away_start_players = new ArrayList<>();
            List<PlayerMatchDto> away_bench_players = new ArrayList<>();
            for (PlayerMatchDto p : hpl) {
                if(p.getPosition() != null )
                    home_start_players.add(p);
                else
                    home_bench_players.add(p);
            }
            for (PlayerMatchDto p : apl) {
                if(p.getPosition() != null )
                    away_start_players.add(p);
                else
                    away_bench_players.add(p);
            }
            model.addAttribute("home_start_players", home_start_players);
            model.addAttribute("home_bench_players", home_bench_players);
            model.addAttribute("away_start_players", away_start_players);
            model.addAttribute("away_bench_players", away_bench_players);
        }
        catch (SQLException sqlException) {
            model.addAttribute("error", sqlException.getMessage());
        }
        return "Match";
    }
}