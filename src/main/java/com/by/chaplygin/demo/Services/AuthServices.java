package com.by.chaplygin.demo.Services;

import com.by.chaplygin.demo.Dto.PersonDto;
import com.by.chaplygin.demo.Enums.PersonRole;
import com.by.chaplygin.demo.Model.Organizer;
import com.by.chaplygin.demo.Model.Person;
import com.by.chaplygin.demo.Repositories.OrganizerRepository;
import com.by.chaplygin.demo.Repositories.PersonRepository;
import com.by.chaplygin.demo.Security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthServices {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final OrganizerRepository organizerRepository;

    public void registration(Person person){
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setActive(true);
        person.setDateOfCreate(LocalDateTime.now());
        person.setRoles(Collections.singletonList(PersonRole.valueOf("USER")));
        person.setScore(0.0);
        personRepository.save(person);
    }

    public ResponseEntity<?> createToken(PersonDto personDto){
        try{
            personRepository.findByUsername(personDto.getUsername());
        } catch (BadCredentialsException e){
            System.out.println("ошибка"); //todo добавить логирвоание

        }
        String token = jwtUtil.generateToken(personDto.getUsername());
        return ResponseEntity.ok(token);
    }

    public ResponseEntity<?> createTokenForOrganizer(Organizer organizer){
        try{
            organizerRepository.findByUsername(organizer.getUsername());
        }catch (BadCredentialsException e){
            System.out.println("gfd");
        }
        String token =jwtUtil.generateToken(organizer.getUsername());
        return ResponseEntity.ok(token);
    }

}
