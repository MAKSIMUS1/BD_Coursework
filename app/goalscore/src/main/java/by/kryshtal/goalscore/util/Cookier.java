package by.kryshtal.goalscore.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Optional;

public class Cookier {
    public static Optional<String> readServletCookie(HttpServletRequest request, String name){
        return Arrays.stream(request.getCookies())
                .filter(cookie->name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findAny();
    }
    public static void deleteCookie(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
