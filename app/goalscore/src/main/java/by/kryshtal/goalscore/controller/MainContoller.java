package by.kryshtal.goalscore.controller;

import by.kryshtal.goalscore.dto.MatchInfoDto;
import by.kryshtal.goalscore.entity.Match;
import by.kryshtal.goalscore.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.List;

@Controller
public class MainContoller {
    private final MatchService matchService;
    @Autowired
    public MainContoller( MatchService matchService) {
        this.matchService = matchService;
    }
    @GetMapping("/")
    public String home(Model model) {
        try{
            List<MatchInfoDto> matches = matchService.getAllMatchInfo();

            model.addAttribute("matches", matches);
        }
        catch (SQLException sqlException) {
            model.addAttribute("error", sqlException.getMessage());
        }
        return "index";
    }
    @GetMapping("/search")
    public String search(@RequestParam(name = "query", required = false) String query, Model model) {
        try{
            List<MatchInfoDto> matches = matchService.getAllMatchInfoBySearch(query);

            model.addAttribute("matches", matches);
        }
        catch (SQLException sqlException) {
            model.addAttribute("error", sqlException.getMessage());
        }
        return "index";
    }
}
