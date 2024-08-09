package by.intexsoft.diplom.auth.controller;

import by.intexsoft.diplom.auth.dto.RegistrationDto;
import by.intexsoft.diplom.auth.service.AuthService;
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

        @PostMapping("/registration")
        public Integer registration(@RequestBody UserRepresentation userRepresentation,
                                           @RequestParam Boolean isOrganizer) {
            return authService.register(userRepresentation, isOrganizer);
        }

        @PutMapping("/verification/{userId}")
        public void verifyEmail(@PathVariable String userId) {
            authService.sendEmailVerification(userId);
        }
}