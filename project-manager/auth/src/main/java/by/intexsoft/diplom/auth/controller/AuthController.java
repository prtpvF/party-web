package by.intexsoft.diplom.auth.controller;

import by.intexsoft.diplom.auth.dto.LoginDto;
import by.intexsoft.diplom.auth.dto.RegistrationDto;
import by.intexsoft.diplom.auth.service.AuthService;
import by.intexsoft.diplom.auth.service.KeycloakService;
import by.intexsoft.diplom.auth.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

       private final AuthService authService;
       private final RefreshTokenService refreshTokenService;
       private final KeycloakService keycloakService;

        @Operation(
                summary = "registration method",
                description = "method saves person in 2 DB: Keycloak DB and Application DB"
        )
        @PostMapping("/registration")
        public HttpStatus registration(@RequestBody RegistrationDto registrationDto) {
            return authService.register(registrationDto);
        }

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody LoginDto loginDto,
                                       HttpServletResponse response) {
                return authService.login(loginDto, response);
        }

        @PostMapping("/refresh")
        public ResponseEntity<?> refreshToken(@CookieValue(value = "refresh-token", required = false) Cookie cookie) {
            return refreshTokenService.refreshToken(cookie);
        }

        @PutMapping("/verification/{userId}")
        public void verifyEmail(@PathVariable String userId) {
            keycloakService.sendEmailVerification(userId);
        }

        @PostMapping("/logout")
        public HttpStatus logout(Principal principal,
                                 HttpServletRequest request) {
            System.out.println(principal.getName());
            return authService.logout(principal, request);
        }

        @GetMapping("/test")
        public String trsd() {
            return "test";
        }
}