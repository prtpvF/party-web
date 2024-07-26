package by.intexsoft.diplom.auth.controller;

import by.intexsoft.diplom.auth.dto.LoginDto;
import by.intexsoft.diplom.auth.dto.RegistrationDto;
import by.intexsoft.diplom.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8090")
@RequestMapping("/auth")
public class AuthController {

        private final AuthService authService;

        @PostMapping("/registration")
        public ResponseEntity registration(@Valid @RequestBody RegistrationDto registrationDto, BindingResult bindingResult) {
            if (bindingResult.hasErrors()) {
                return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
            }
            authService.register(registrationDto);
            return new ResponseEntity("success", HttpStatus.CREATED);
        }

        @Operation(summary = "Get a user by Id")
        @PostMapping("/login")
        public String login(@RequestBody LoginDto loginDto) {
            return authService.login(loginDto);
        }

        @SecurityRequirement(name = "token")
        @GetMapping("/logout")
        public void logout(HttpServletRequest request, HttpServletResponse response, @RequestHeader("token") String token) {
            authService.logout(request, response, token);
        }
}