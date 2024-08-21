package by.intexsoft.diplom.auth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CookieService {

        public void addRefreshTokenInCookie(String cookieName,
                                              String refreshToken,
                                              HttpServletResponse response) {

            Cookie cookie = new Cookie(cookieName, refreshToken);
            cookie.setPath("/auth/refresh");
            cookie.setHttpOnly(true);
            cookie.setMaxAge((int) Duration.ofDays(7).getSeconds());
            response.addCookie(cookie);
        }
}
