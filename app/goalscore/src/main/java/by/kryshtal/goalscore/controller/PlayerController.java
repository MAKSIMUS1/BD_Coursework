package by.kryshtal.goalscore.controller;

import by.kryshtal.goalscore.dto.PlayerInfoDto;
import by.kryshtal.goalscore.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.List;

@Controller
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/players")
    public String showPlayerList(@RequestParam(defaultValue = "0") int page, Model model) throws SQLException {
        try {
            if(page == 0)
                model.addAttribute("first", true);
            int maxPages = playerService.getPlayersPages();
            if(page == maxPages)
                model.addAttribute("last", true);

            model.addAttribute("number", page);
            model.addAttribute("totalPages", maxPages);
            List<PlayerInfoDto> players = playerService.getPlayersInRange(page*100);
            model.addAttribute("players", players);
        }
        catch (SQLException sqlException) {
            model.addAttribute("error", sqlException.getMessage());
        }
        return "Players";
    }
}
