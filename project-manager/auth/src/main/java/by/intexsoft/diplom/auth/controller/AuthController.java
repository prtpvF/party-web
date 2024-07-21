package by.intexsoft.diplom.auth.controller;

import by.intexsoft.diplom.auth.dto.LogInDto;
import by.intexsoft.diplom.auth.dto.RegistrationDto;
import by.intexsoft.diplom.auth.service.AuthService;
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

    @PostMapping("/login")
    public String login(@RequestBody LogInDto logInDto) {
        return authService.login(logInDto);
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response, @RequestHeader("token") String token) {
        authService.logout(request, response, token);
    }


}