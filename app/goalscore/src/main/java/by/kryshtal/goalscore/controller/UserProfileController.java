package by.kryshtal.goalscore.controller;

import by.kryshtal.goalscore.dto.TeamDto;
import by.kryshtal.goalscore.exceptions.NoSuchEntityException;
import by.kryshtal.goalscore.service.UserService;
import by.kryshtal.goalscore.util.Cookier;
import by.kryshtal.goalscore.util.Mapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@Controller
public class UserProfileController {

    private final UserService userService;
    @Autowired
    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = {"/user_profile"})
    public ModelAndView userProfile(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        ModelAndView mav = new ModelAndView();
        response.setHeader("Content-Type", "text/html; charset=utf-8");
        try{
            int user_id = userService.getUserId(request);
            if(user_id == -1)
                throw new NoSuchEntityException("bad user id");
            mav.getModelMap().addAttribute("user", userService.getUserProfile(user_id));
        } catch (NoSuchEntityException | SQLException e) {
            mav.getModelMap().addAttribute("error", e.getMessage());
        }
        mav.setViewName("UserProfile");
        return mav;
    }
    @PostMapping(value = {"/exit_account"})
    public String exitFromAccount(HttpServletRequest request, HttpServletResponse response) {
        Cookier.deleteCookie("user-id", response);
        return "redirect:/";
    }

    @PutMapping("/upload_profile_image")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request, Model model) {
        try {
            int user_id = userService.getUserId(request);
            if (user_id == -1)
                throw new NoSuchEntityException("bad user id");

            if (!file.isEmpty()) {
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image")) {
                    model.addAttribute("error", "Please select a valid image file");
                    return ResponseEntity.badRequest().body("Please select a valid image file");
                }

                byte[] bytes = file.getBytes();
                userService.setProfile_image(user_id, bytes);
                model.addAttribute("message", "File uploaded successfully");
                return ResponseEntity.ok("File uploaded successfully");
            } else {
                model.addAttribute("error", "Please select a file to upload");
                return ResponseEntity.badRequest().body("Please select a file to upload");
            }
        } catch (IOException | SQLException | NoSuchEntityException e) {
            model.addAttribute("error", "Error during file upload: " + e.getMessage());
            return ResponseEntity.status(500).body("Error during file upload: " + e.getMessage());
        }
    }
}

