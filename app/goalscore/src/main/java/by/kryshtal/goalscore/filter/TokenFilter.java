package by.kryshtal.goalscore.filter;

import by.kryshtal.goalscore.util.Cookier;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public class TokenFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            String uri = httpRequest.getRequestURI();
            Optional<String> optionalToken = Cookier.readServletCookie(httpRequest, "user-id");

            if (optionalToken.isPresent()) {
                chain.doFilter(request, response);
            }else if("/api/authenticate".equals(uri) ||
                    "/".equals(uri) ||
                    "/search".equals(uri) ||
                    "/api/registration".equals(uri) ||
                    "/api/goalscore/registration".equals(uri) ||
                    "/api/goalscore/authenticate".equals(uri) ){
                chain.doFilter(request, response);
            }else {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/api/authenticate");
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
