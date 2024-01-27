package com.by.chaplygin.demo.Services;

import com.by.chaplygin.demo.Enums.PersonRole;
import com.by.chaplygin.demo.Model.Person;
import com.by.chaplygin.demo.Repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationServices {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public void registration(Person person){
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setActive(true);
        person.setDateOfCreate(LocalDateTime.now());
        person.setRoles(Collections.singletonList(PersonRole.valueOf("USER")));
        person.setScore(0.0);
        personRepository.save(person);
    }
}
