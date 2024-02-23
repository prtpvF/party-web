package com.auth.authmicroservice.Service;

import com.auth.authmicroservice.DTO.OrganizerDto;
import com.auth.authmicroservice.DTO.PersonDto;
import com.auth.authmicroservice.Exceptions.PersonNotFoundException;
import com.auth.authmicroservice.Model.Person;
import com.auth.authmicroservice.PersonRole;
import com.auth.authmicroservice.Repository.OrganizerRepository;
import com.auth.authmicroservice.Repository.PersonRepository;
import com.auth.authmicroservice.Security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final OrganizerRepository organizerRepository;
    private  final AuthenticationManager authenticationManager;
    private  final RequestsServices requestsServices;

    public void registration(Person person){
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setActive(true);
        person.setDateOfCreate(LocalDateTime.now());
        person.setRoles(Collections.singletonList(PersonRole.valueOf("USER")));
        person.setScore(0.0);
        personRepository.save(person);
        requestsServices.sendRequestToMailService(person.getEmail(), "fds",null,"fsd");
    }

    public ResponseEntity<?> createToken(@RequestBody PersonDto personDto) throws PersonNotFoundException {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(personDto.getUsername(), personDto.getPassword()));
        }catch (BadCredentialsException e){
            throw new PersonNotFoundException("неверный логин или пароль");
        }
        String token = jwtUtil.generateToken(personDto.getUsername());
        return ResponseEntity.ok(token);
    }

    public ResponseEntity<?> createTokenForOrganizer(@RequestBody OrganizerDto organizerDto) throws PersonNotFoundException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(organizerDto.getUsername(), organizerDto.getPassword()));
        }catch (BadCredentialsException e){
            throw new PersonNotFoundException("неверный логин или пароль");
        }
        String token =jwtUtil.generateToken(organizerDto.getUsername());
        return ResponseEntity.ok(token);
    }

}