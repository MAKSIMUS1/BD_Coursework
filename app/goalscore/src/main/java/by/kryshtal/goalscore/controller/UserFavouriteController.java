package by.kryshtal.goalscore.controller;

import by.kryshtal.goalscore.dto.PlayerInfoDto;
import by.kryshtal.goalscore.dto.TeamInfoDto;
import by.kryshtal.goalscore.entity.Player;
import by.kryshtal.goalscore.entity.UserFavoritePlayer;
import by.kryshtal.goalscore.entity.UserFavoriteTeam;
import by.kryshtal.goalscore.entity.UserStarTeam;
import by.kryshtal.goalscore.exceptions.NoSuchEntityException;
import by.kryshtal.goalscore.service.*;
import by.kryshtal.goalscore.util.Mapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class UserFavouriteController {

    private final UserService userService;
    private final UserFavouriteService userFavouriteService;
    private final TeamService teamService;
    private final AreaService areaService;
    private final PlayerService playerService;
    @Autowired
    public UserFavouriteController(UserService userService, UserFavouriteService userFavouriteService,
                                   TeamService teamService, AreaService areaService,
                                   PlayerService playerService) {
        this.userService = userService;
        this.userFavouriteService = userFavouriteService;
        this.teamService = teamService;
        this.areaService = areaService;
        this.playerService = playerService;
    }

    @GetMapping(value = {"/favourite"})
    public String getFavouritePage(HttpServletRequest request, Model model) {
        return "Favourite";
    }

    @GetMapping(value = {"/favourite-teams"})
    public String getFavouriteTeamsPage(HttpServletRequest request, Model model){
        try {
            int user_id = userService.getUserId(request);
            if (user_id == -1)
                throw new NoSuchEntityException("bad user id");

            List<TeamInfoDto> teams = new ArrayList<>();
            List<UserFavoriteTeam> user_favorite_teams = userFavouriteService.getFavouriteTeams(user_id);
            for (UserFavoriteTeam team : user_favorite_teams) {
                TeamInfoDto teamInfo = Mapper.map(teamService.getTeamById(team.getTeam_id()), TeamInfoDto.class);
                teamInfo.setArea_name(areaService.getAreaById(teamInfo.getArea_id()).getName());
                teams.add(teamInfo);
            }
            model.addAttribute("favourite_teams", teams);
        } catch (SQLException | NoSuchEntityException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "FavouriteTeams";
    }

    @GetMapping(value = {"/favourite-players"})
    public String getFavouritePlayersPage(HttpServletRequest request, Model model) {
        try {
            int user_id = userService.getUserId(request);
            if (user_id == -1)
                throw new NoSuchEntityException("bad user id");
            model.addAttribute("favourite_players", userFavouriteService.getFavouritePlayers(user_id));
        } catch (SQLException | NoSuchEntityException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "FavouritePlayers";
    }
    @GetMapping(value = {"/favourite/build-team"})
    public String getTeamBuilderPage(HttpServletRequest request, Model model) {
        try {
            int user_id = userService.getUserId(request);
            if (user_id == -1)
                throw new NoSuchEntityException("bad user id");
            List<PlayerInfoDto> allFavouritePlayers = userFavouriteService.getFavouritePlayers(user_id);
            List<PlayerInfoDto> goalkeepers = filterPlayersByPosition(allFavouritePlayers, "Goalkeeper");
            List<PlayerInfoDto> offences = filterPlayersByPosition(allFavouritePlayers, "Offence");
            List<PlayerInfoDto> midfielders = filterPlayersByPosition(allFavouritePlayers, "Midfield");
            List<PlayerInfoDto> defenders = filterPlayersByPosition(allFavouritePlayers, "Defence");
            model.addAttribute("goalkeepers", goalkeepers);
            model.addAttribute("defenders", defenders);
            model.addAttribute("midfielders", midfielders);
            model.addAttribute("offences", offences);
        } catch (SQLException | NoSuchEntityException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "BuildTeam";
    }

    @GetMapping(value = {"/favourite/user-team"})
    public String getUserTeamPage(HttpServletRequest request, Model model) {
        try{
            int user_id = userService.getUserId(request);
            if (user_id == -1)
                throw new NoSuchEntityException("bad user id");


            UserStarTeam star_team = userFavouriteService.getUserStarTeam(user_id);

            Player goalkeeper = playerService.getPlayerById(star_team.getGoalkeeper_id());

            Player l_def = playerService.getPlayerById(star_team.getLeft_defender_id());
            Player def_1 = playerService.getPlayerById(star_team.getCentral_2_defender_id());
            Player def_2 = playerService.getPlayerById(star_team.getCentral_2_defender_id());
            Player r_def = playerService.getPlayerById(star_team.getRight_defender_id());

            Player l_mid = playerService.getPlayerById(star_team.getLeft_midfielder_id());
            Player c_mid = playerService.getPlayerById(star_team.getCentral_midfielder_id());
            Player r_mid = playerService.getPlayerById(star_team.getRight_midfielder_id());

            Player l_winger = playerService.getPlayerById(star_team.getLeft_winger_id());
            Player striker = playerService.getPlayerById(star_team.getStriker_id());
            Player r_winger = playerService.getPlayerById(star_team.getRight_winger_id());

            model.addAttribute("g", goalkeeper);

            model.addAttribute("ld", l_def);
            model.addAttribute("cdf", def_1);
            model.addAttribute("cds", def_2);
            model.addAttribute("rd", r_def);

            model.addAttribute("lm", l_mid);
            model.addAttribute("cm", c_mid);
            model.addAttribute("rm", r_mid);

            model.addAttribute("lw", l_winger);
            model.addAttribute("striker", striker);
            model.addAttribute("rw", r_winger);

        }   catch (SQLException | NoSuchEntityException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "UniqueTeam";
    }

    @PostMapping("/favourite/addToFavoritesPlayer")
    public ResponseEntity<Object> addToFavoritesPlayer(@RequestBody PlayerRequest playerRequest, HttpServletRequest request, Model model) {
        try {
            int user_id = userService.getUserId(request);
            if (user_id == -1)
                throw new NoSuchEntityException("bad user id");

            UserFavoritePlayer user_player = new UserFavoritePlayer();
            user_player.setUser_id(user_id);
            user_player.setPlayer_id(Math.toIntExact(playerRequest.getPlayerId()));
            userFavouriteService.addFavouritePlayer(user_player);

            return ResponseEntity.ok().body(Map.of("message", "Player added to favorites successfully"));
        } catch (SQLException | NoSuchEntityException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/favorite/addToFavoritesTeam")
    public ResponseEntity<Object> addToFavoritesTeam(@RequestBody TeamRequest teamRequest, HttpServletRequest request, Model model) {
        try {
            int user_id = userService.getUserId(request);
            if (user_id == -1)
                throw new NoSuchEntityException("bad user id");

            UserFavoriteTeam user_team = new UserFavoriteTeam();
            user_team.setUser_id(user_id);
            user_team.setTeam_id(teamRequest.getTeamId());
            userFavouriteService.addFavouriteTeam(user_team);

            return ResponseEntity.ok().body(Map.of("message", "Team added to favorites successfully"));
        } catch (SQLException | NoSuchEntityException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/favourite/build_team")
    public String handleBuildTeam(
            @RequestParam("goalkeeper") int goalkeeper,
            @RequestParam("leftDefender") int leftDefender,
            @RequestParam("central1Defender") int central1Defender,
            @RequestParam("central2Defender") int central2Defender,
            @RequestParam("rightDefender") int rightDefender,
            @RequestParam("leftMidfielder") int leftMidfielder,
            @RequestParam("centralMidfielder") int centralMidfielder,
            @RequestParam("rightMidfielder") int rightMidfielder,
            @RequestParam("leftWinger") int leftWinger,
            @RequestParam("striker") int striker,
            @RequestParam("rightWinger") int rightWinger,
            HttpServletRequest request,
            Model model) {
        try {
            int user_id = userService.getUserId(request);
            if (user_id == -1)
                throw new NoSuchEntityException("bad user id");

            userFavouriteService.buildUserTeam(user_id,
                    goalkeeper,
                    leftDefender,
                    central1Defender,
                    central2Defender,
                    rightDefender,
                    leftMidfielder,
                    centralMidfielder,
                    rightMidfielder,
                    leftWinger,
                    striker,
                    rightWinger);
        }catch (NoSuchEntityException | SQLException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        return "redirect:/favourite/user-team";
    }

    @DeleteMapping("/favourite/removeFromFavorites")
    public ResponseEntity<String> removeFromFavoritesPlayer(@RequestBody PlayerRequest playerRequest, HttpServletRequest request, Model model) {
        try {
            int user_id = userService.getUserId(request);
            if (user_id == -1)
                throw new NoSuchEntityException("bad user id");
            userFavouriteService.removeFavoritePlayer(user_id, Math.toIntExact(playerRequest.getPlayerId()));
            return new ResponseEntity<>("Player removed from favorites successfully", HttpStatus.OK);
        } catch (NoSuchEntityException | SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/favorite/removeFromFavoritesTeam")
    public ResponseEntity<String> removeFromFavoritesTeam(@RequestBody TeamRequest teamRequest, HttpServletRequest request, Model model) {
        try {
            int user_id = userService.getUserId(request);
            if (user_id == -1)
                throw new NoSuchEntityException("bad user id");

            userFavouriteService.removeFavoriteTeam(user_id, teamRequest.getTeamId());
            return new ResponseEntity<>("Team removed from favorites successfully", HttpStatus.OK);
        } catch (NoSuchEntityException | SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    private static class PlayerRequest {
        private Long playerId;

        public Long getPlayerId() {
            return playerId;
        }

        public void setPlayerId(Long playerId) {
            this.playerId = playerId;
        }
    }
    private static class TeamRequest {
        private int teamId;

        public int getTeamId() {
            return teamId;
        }

        public void setTeamId(int teamId) {
            this.teamId = teamId;
        }
    }
    private List<PlayerInfoDto> filterPlayersByPosition(List<PlayerInfoDto> players, String position) {
        return players.stream()
                .filter(player -> position.equals(player.getPosition()))
                .collect(Collectors.toList());
    }
}

