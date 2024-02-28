package com.auth.authmicroservice.Controller;

import com.auth.authmicroservice.DTO.OrganizerDto;
import com.auth.authmicroservice.DTO.PersonDto;
import com.auth.authmicroservice.Exceptions.IllegalAgeException;
import com.auth.authmicroservice.Exceptions.PersonNotFoundException;
import com.auth.authmicroservice.Model.Person;
import com.auth.authmicroservice.Security.JwtUtil;
import com.auth.authmicroservice.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final ModelMapper modelMapper;
    private final AuthService authServices;
    private final JwtUtil jwtUtil;

    @PostMapping("/registration")
    public Map<String, String> registration(@RequestBody PersonDto personDto){
        Person person = converToPerson(personDto);
        try {
            authServices.registration(person);
        } catch (IllegalAgeException e) {
            return Map.of("error", "you are to young");
        }
        String token = jwtUtil.generateToken(person.getUsername());
        return Map.of("jwt-tokens", token);
    }


    public Person converToPerson(PersonDto personDto){
        return this.modelMapper.map(personDto, Person.class);
    }


    @PostMapping("/token")
    public ResponseEntity<?> createToken(@RequestBody PersonDto personDto) throws PersonNotFoundException {
        return authServices.createToken(personDto);
    }
    @PostMapping("/organizer/token")
    public ResponseEntity<?> createOrganizerToken(@RequestBody OrganizerDto organizer) throws PersonNotFoundException {
        return  authServices.createTokenForOrganizer(organizer);
    }



}