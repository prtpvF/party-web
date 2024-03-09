package com.auth.authmicroservice.Service;

import com.auth.authmicroservice.DTO.OrganizerDto;
import com.auth.authmicroservice.DTO.PersonDto;
import com.auth.authmicroservice.Exceptions.IllegalAgeException;
import com.auth.authmicroservice.Exceptions.PersonNotFoundException;
import com.auth.authmicroservice.Model.Person;
import com.auth.authmicroservice.PersonRole;
import com.auth.authmicroservice.Repository.OrganizerRepository;
import com.auth.authmicroservice.Repository.PersonRepository;
import com.auth.authmicroservice.Security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.ResourceAccessException;

import java.net.ConnectException;
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


    public void registration(Person person) throws IllegalAgeException {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setActive(true);
        person.setDateOfCreate(LocalDateTime.now());
        person.setRoles(Collections.singletonList(PersonRole.valueOf("USER")));
        person.setScore(0.0);
        if(person.getAge()<16) {
                throw new IllegalAgeException("you are to young");
        }
        else {
            personRepository.save(person);
            try{
                HttpStatusCode status = requestsServices.sendRequestToMailService(person.getEmail(), "fds", null, "REGISTRATION");
            }catch (ResourceAccessException e){
                //todo добавить логирование
            }
        }
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