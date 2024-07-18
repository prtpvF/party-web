package by.intexsoft.diplom.security.filter;


import by.intexsoft.diplom.security.jwt.JwtUtil;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter implements Filter {
    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String token = jwtUtil.extractTokenFromHeader(request);
        try {
            if (token != null) {
                jwtUtil.isTokenActive(token);
                String username = jwtUtil.validateTokenAndRetrieveClaim(token);
                log.info("Token is valid for user: {}", username);
                request.setAttribute("token", token);
            }

            filterChain.doFilter(servletRequest, servletResponse);
        } catch (JWTVerificationException e) {
            log.error("JWT verification failed: {}", e.getMessage());
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Invalid JWT token.");
        } catch (Exception e) {
            log.error("An error occurred during token processing: {}", e.getMessage());
            sendErrorResponse(response, HttpStatus.BAD_REQUEST, "Something went wrong during token processing.");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.format("{\"error\": \"%s\"}", message));
    }
}