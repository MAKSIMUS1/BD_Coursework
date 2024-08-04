package by.kryshtal.goalscore.controller;

import by.kryshtal.goalscore.dto.AuthUserDto;
import by.kryshtal.goalscore.dto.RegisterUserDto;
import by.kryshtal.goalscore.exceptions.AuthenticationException;
import by.kryshtal.goalscore.exceptions.NoSuchEntityException;
import by.kryshtal.goalscore.exceptions.RegistrationException;
import by.kryshtal.goalscore.service.UserService;
import by.kryshtal.goalscore.util.Cookier;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.Optional;

@Controller
@RequestMapping("api")
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = {"/registration"})
    public ModelAndView registration(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mav = new ModelAndView();
        response.setHeader("Content-Type", "text/html; charset=utf-8");
        mav.getModelMap().addAttribute("registerUserDto", new RegisterUserDto());
        mav.setViewName("Registration");

        return mav;
    }
    @PostMapping(value = {"/goalscore/registration"})
    public ModelAndView registrationPost(@Valid RegisterUserDto regDto) throws SQLException, RegistrationException {
        ModelAndView mav = new ModelAndView("Registration");
        try {
            if (!(regDto.getLogin().length() >= 2 && regDto.getLogin().length() <= 32 &&
                    !regDto.getLogin().contains(" "))) {
                throw new RegistrationException("Not a correct login");
            }
            if (!(regDto.getPassword().length() >= 2 && regDto.getPassword().length() <= 32 &&
                    !regDto.getPassword().contains(" "))) {
                throw new RegistrationException("Not a correct password");
            }
            if (!regDto.getPhone().matches("\\d{12}")) {
                throw new RegistrationException("Not a valid phone number");
            }

            userService.register(regDto);
            mav.getModelMap().addAttribute("registrationSuccess", true);
        }
        catch (SQLException | RegistrationException sqlException) {
            mav.getModelMap().addAttribute("error", sqlException.getMessage());
        }
        return mav;
    }
    @GetMapping(value = {"/authenticate"})
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mav = new ModelAndView();
        response.setHeader("Content-Type", "text/html; charset=utf-8");
        mav.getModelMap().addAttribute("authUserDto", new AuthUserDto());
        mav.setViewName("Authenticate");

        return mav;
    }
    @PostMapping(value = {"/goalscore/authenticate"})
    public ModelAndView authenticate(@Valid AuthUserDto authUserDto, HttpServletRequest request, HttpServletResponse response) throws SQLException, RegistrationException {
        ModelAndView mav = new ModelAndView("Authenticate");
        try {
            String token = userService.authenticate(authUserDto);

            Cookie jwtTokenCookie = new Cookie("user-id", token);

            jwtTokenCookie.setMaxAge(86400);
            jwtTokenCookie.setHttpOnly(true);
            jwtTokenCookie.setPath("/");

            response.addCookie(jwtTokenCookie);

            mav.getModelMap().addAttribute("authenticateSuccess", true);
        }
        catch (SQLException | AuthenticationException | NoSuchEntityException sqlException) {
            mav.getModelMap().addAttribute("error", sqlException.getMessage());
        }
        return mav;
    }

}
