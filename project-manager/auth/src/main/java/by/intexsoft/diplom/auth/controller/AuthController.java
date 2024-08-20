package by.intexsoft.diplom.auth.controller;

import by.intexsoft.diplom.auth.dto.AuthenticationDTOResponse;
import by.intexsoft.diplom.auth.dto.LoginDto;
import by.intexsoft.diplom.auth.dto.RegistrationDto;
import by.intexsoft.diplom.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

       private final AuthService authService;

        @Operation(
                summary = "registration method",
                description = "method saves person in 2 DB: Keycloak DB and Application DB"
        )
        @PostMapping("/registration")
        public HttpStatus registration(@RequestBody RegistrationDto registrationDto) {
            return authService.register(registrationDto);
        }

        @PostMapping("/login")
        public AuthenticationDTOResponse login(@RequestBody LoginDto loginDto) {
                return authService.login(loginDto);
        }

        @PutMapping("/verification/{userId}")
        public void verifyEmail(@PathVariable String userId) {
            authService.sendEmailVerification(userId);
        }
}