package com.by.chaplygin.demo.Services;

import com.by.chaplygin.demo.Dto.PersonDto;
import com.by.chaplygin.demo.Exceptions.PersonNotFoundException;
import com.by.chaplygin.demo.Model.Party;
import com.by.chaplygin.demo.Model.Person;
import com.by.chaplygin.demo.Repositories.PartyRepository;
import com.by.chaplygin.demo.Repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartyServices {
    private final PartyRepository partyRepository;
    private final PersonRepository personRepository;

    public void createParty(String username, Party party) throws PersonNotFoundException {
            Optional<Person> person = personRepository.findByUsername(username);
            if(!person.isPresent()){
                throw new PersonNotFoundException("Person doesnt exists");
            }
            party.setDateOfCreate(LocalDateTime.now());
            party.setOrganizer(person.get());
        partyRepository.save(party);
    }
}
