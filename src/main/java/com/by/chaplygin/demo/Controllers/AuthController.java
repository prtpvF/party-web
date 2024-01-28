package com.by.chaplygin.demo.Controllers;

import com.by.chaplygin.demo.Dto.PersonDto;
import com.by.chaplygin.demo.Model.Person;
import com.by.chaplygin.demo.Security.JwtUtil;
import com.by.chaplygin.demo.Services.AuthServices;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final ModelMapper modelMapper;
    private final AuthServices authServices;
    private final JwtUtil jwtUtil;

    @PostMapping("/registration")
    public Map<String, String> registration(@RequestBody PersonDto personDto){
        Person person = converToPerson(personDto);
        authServices.registration(person);
        String token = jwtUtil.generateToken(person.getUsername());
        return Map.of("jwt-tokens", token);
    }
    public Person converToPerson(PersonDto personDto){
        return this.modelMapper.map(personDto, Person.class);
    }
    @PostMapping("/token")
    public ResponseEntity<?> createToken(@RequestBody PersonDto personDto){
       return authServices.createToken(personDto);
    }
}
