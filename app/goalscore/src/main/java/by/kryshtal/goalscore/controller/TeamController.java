package by.kryshtal.goalscore.controller;

import by.kryshtal.goalscore.dto.TeamDto;
import by.kryshtal.goalscore.entity.Area;
import by.kryshtal.goalscore.entity.Team;
import by.kryshtal.goalscore.service.AreaService;
import by.kryshtal.goalscore.service.TeamService;
import by.kryshtal.goalscore.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.List;

@Controller
public class TeamController {
    private final TeamService teamService;
    private final AreaService areaService;
    @Autowired
    public TeamController(TeamService teamService, AreaService areaService) {
        this.teamService = teamService;
        this.areaService = areaService;
    }

    @GetMapping(value = {"/teamList"})
    public ModelAndView teamListPage() throws SQLException {
        ModelAndView mav = new ModelAndView("TeamsViewer");
        List<TeamDto> t = Mapper.mapAll(teamService.getAllTeams(), TeamDto.class);
        mav.getModelMap().addAttribute("teams", Mapper.mapAll(teamService.getAllTeams(), TeamDto.class));
        return mav;
    }

    @GetMapping("/team/{teamId}")
    public String getTeamPage(@PathVariable int teamId, Model model) throws SQLException {
        Team team = teamService.getTeamById(teamId);
        Area area = areaService.getAreaById(team.getArea_id());
        model.addAttribute("team", team);
        model.addAttribute("area", area);

        return "Team";
    }
}
