package by.kryshtal.goalscore.controller;

import by.kryshtal.goalscore.dto.*;
import by.kryshtal.goalscore.entity.Player;
import by.kryshtal.goalscore.entity.Team;
import by.kryshtal.goalscore.exceptions.AuthenticationException;
import by.kryshtal.goalscore.exceptions.NoSuchEntityException;
import by.kryshtal.goalscore.exceptions.RegistrationException;
import by.kryshtal.goalscore.service.*;
import by.kryshtal.goalscore.util.Cookier;
import by.kryshtal.goalscore.util.Mapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.SQLException;
import java.text.ParseException;

@Controller
@RequestMapping("admin")
public class AdminController {
    private final UserService userService;
    private final TeamApplicationService teamApplicationService;
    private final MatchService matchService;
    private final TeamService teamService;
    private final PlayerService playerService;
    @Autowired
    public AdminController(UserService userService, TeamApplicationService teamApplicationService,
                           MatchService matchService, TeamService teamService,
                           PlayerService playerService) {
        this.userService = userService;
        this.teamApplicationService = teamApplicationService;
        this.matchService = matchService;
        this.teamService = teamService;
        this.playerService = playerService;
    }
    @GetMapping(value = {"/panel"})
    public  ModelAndView adminPanel(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        if(userService.isAdmin(request)) {
            ModelAndView mav = new ModelAndView();
            response.setHeader("Content-Type", "text/html; charset=utf-8");
            mav.setViewName("/admin/Admin");
            return mav;
        }
        return new ModelAndView("index");
    }
    @GetMapping(value = {"/create-team-application"})
    public  ModelAndView createTeamApplicationPage(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        if(userService.isAdmin(request)) {
            ModelAndView mav = new ModelAndView();
            response.setHeader("Content-Type", "text/html; charset=utf-8");
            mav.getModelMap().addAttribute("create_teamApplicationDto", new TeamApplicationDto());
            mav.setViewName("/admin/CreateTeamApplication");
            return mav;
        }
        return new ModelAndView("index");
    }
    @GetMapping(value = {"/create-match"})
    public  ModelAndView createMatchPage(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        if(userService.isAdmin(request)) {
            ModelAndView mav = new ModelAndView();
            response.setHeader("Content-Type", "text/html; charset=utf-8");
            mav.getModelMap().addAttribute("create_match", new MatchDto());
            mav.setViewName("/admin/CreateMatch");
            return mav;
        }
        return new ModelAndView("index");
    }
    @GetMapping(value = {"/create-team"})
    public  ModelAndView createTeamPage(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        if(userService.isAdmin(request)) {
            ModelAndView mav = new ModelAndView();
            response.setHeader("Content-Type", "text/html; charset=utf-8");
            mav.getModelMap().addAttribute("create_team", new Team());
            mav.setViewName("/admin/CreateTeam");
            return mav;
        }
        return new ModelAndView("index");
    }
    @GetMapping(value = {"/update-team"})
    public  ModelAndView updateTeamPage(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        if(userService.isAdmin(request)) {
            ModelAndView mav = new ModelAndView();
            response.setHeader("Content-Type", "text/html; charset=utf-8");
            mav.getModelMap().addAttribute("update_team", new Team());
            mav.setViewName("/admin/UpdateTeam");
            return mav;
        }
        return new ModelAndView("index");
    }
    @GetMapping(value = {"/delete-team"})
    public  ModelAndView deleteTeamPage(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        if(userService.isAdmin(request)) {
            ModelAndView mav = new ModelAndView();
            response.setHeader("Content-Type", "text/html; charset=utf-8");
            mav.getModelMap().addAttribute("delete_team", new Team());
            mav.setViewName("/admin/DeleteTeam");
            return mav;
        }
        return new ModelAndView("index");
    }
    //-----player get
    @GetMapping(value = {"/create-player"})
    public  ModelAndView createPlayerPage(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        if(userService.isAdmin(request)) {
            ModelAndView mav = new ModelAndView();
            response.setHeader("Content-Type", "text/html; charset=utf-8");
            mav.getModelMap().addAttribute("create_player", new PlayerDto());
            mav.setViewName("/admin/CreatePlayer");
            return mav;
        }
        return new ModelAndView("index");
    }
    @GetMapping(value = {"/update-player"})
    public  ModelAndView updatePlayerPage(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        if(userService.isAdmin(request)) {
            ModelAndView mav = new ModelAndView();
            response.setHeader("Content-Type", "text/html; charset=utf-8");
            mav.getModelMap().addAttribute("update_player", new PlayerDto());
            mav.setViewName("/admin/UpdatePlayer");
            return mav;
        }
        return new ModelAndView("index");
    }
    @GetMapping(value = {"/delete-player"})
    public  ModelAndView deletePlayerPage(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        if(userService.isAdmin(request)) {
            ModelAndView mav = new ModelAndView();
            response.setHeader("Content-Type", "text/html; charset=utf-8");
            mav.getModelMap().addAttribute("delete_player", new PlayerDto());
            mav.setViewName("/admin/DeletePlayer");
            return mav;
        }
        return new ModelAndView("index");
    }
    //---------------------------------
    @PostMapping(value = {"/create/team-application"})
    public ModelAndView createTeamApplication(TeamApplicationDto teamReqDto)  {
        ModelAndView mav = new ModelAndView("/admin/CreateTeamApplication");
        mav.getModelMap().addAttribute("create_teamApplicationDto", new TeamApplicationDto());
        try{
            teamApplicationService.createTeamApplication(teamReqDto);
            mav.getModelMap().addAttribute("result", "success");
        }
        catch (SQLException sqlException) {
            mav.getModelMap().addAttribute("error", sqlException.getMessage());
        }
        return mav;
    }
    @PostMapping(value = {"/create/match"})
    public ModelAndView createMatch(MatchDto matchDto)  {
        ModelAndView mav = new ModelAndView("/admin/CreateMatch");
        mav.getModelMap().addAttribute("create_match", new MatchDto());
        try{
            matchService.createMatch(matchDto);
            mav.getModelMap().addAttribute("result", "success");
        }
        catch (SQLException | ParseException sqlException) {
            mav.getModelMap().addAttribute("error", sqlException.getMessage());
        }
        return mav;
    }
    @PostMapping(value = {"/create/team"})
    public ModelAndView createTeam(Team team)  {
        ModelAndView mav = new ModelAndView("/admin/CreateTeam");
        mav.getModelMap().addAttribute("create_team", new Team());
        try{
            teamService.createTeam(team);
            mav.getModelMap().addAttribute("result", "success");
        }
        catch (SQLException sqlException) {
            mav.getModelMap().addAttribute("error", sqlException.getMessage());
        }
        return mav;
    }
    @PutMapping(value = {"/update/team"})
    public ModelAndView updateTeam(Team team)  {
        ModelAndView mav = new ModelAndView("/admin/UpdateTeam");
        mav.getModelMap().addAttribute("update_team", new Team());
        try{
            teamService.updateTeam(team);
            mav.getModelMap().addAttribute("result", "success");
        }
        catch (SQLException sqlException) {
            mav.getModelMap().addAttribute("error", sqlException.getMessage());
        }
        return mav;
    }
    @DeleteMapping(value = {"/delete/team"})
    public ModelAndView deleteTeam(Team team)  {
        ModelAndView mav = new ModelAndView("/admin/DeleteTeam");
        mav.getModelMap().addAttribute("delete_team", new Team());
        try{
            teamService.removeTeam(team);
            mav.getModelMap().addAttribute("result", "success");
        }
        catch (SQLException sqlException) {
            mav.getModelMap().addAttribute("error", sqlException.getMessage());
        }
        return mav;
    }
    //player
    @PostMapping(value = {"/create/player"})
    public ModelAndView createPlayer( PlayerDto player)  {
        ModelAndView mav = new ModelAndView("/admin/CreatePlayer");
        mav.getModelMap().addAttribute("create_player", new PlayerDto());
        try{
            playerService.createPlayer(Mapper.map(player, Player.class));
            mav.getModelMap().addAttribute("result", "success");
        }
        catch (SQLException | ParseException sqlException) {
            mav.getModelMap().addAttribute("error", sqlException.getMessage());
        }
        return mav;
    }
    @PutMapping(value = {"/update/player"})
    public ModelAndView updatePlayer(PlayerDto player)  {
        ModelAndView mav = new ModelAndView("/admin/UpdatePlayer");
        mav.getModelMap().addAttribute("update_player", new PlayerDto());
        try{
            playerService.updatePlayer(Mapper.map(player, Player.class));
            mav.getModelMap().addAttribute("result", "success");
        }
        catch (SQLException sqlException) {
            mav.getModelMap().addAttribute("error", sqlException.getMessage());
        }
        return mav;
    }
    @DeleteMapping(value = {"/delete/player"})
    public ModelAndView deletePlayer(PlayerDto player)  {
        ModelAndView mav = new ModelAndView("/admin/DeletePlayer");
        mav.getModelMap().addAttribute("delete_player", new PlayerDto());
        try{
            playerService.removePlayer(Mapper.map(player, Player.class));
            mav.getModelMap().addAttribute("result", "success");
        }
        catch (SQLException sqlException) {
            mav.getModelMap().addAttribute("error", sqlException.getMessage());
        }
        return mav;
    }
}
