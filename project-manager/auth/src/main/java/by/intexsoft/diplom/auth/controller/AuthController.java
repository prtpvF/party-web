package by.intexsoft.diplom.auth.controller;

import by.intexsoft.diplom.auth.dto.RegistrationDto;
import by.intexsoft.diplom.common_module.jwt.JwtToken;
import by.intexsoft.diplom.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtToken jwtToken;

    @PostMapping("/registration")
    public HttpStatus registration(@RequestBody RegistrationDto registrationDto){
        authService.register(registrationDto);
        return HttpStatus.CREATED;
    }

    @PostMapping("/token")
    public String getToken(@RequestBody String username){
        return authService.createJwtToken(username);
    }

}
