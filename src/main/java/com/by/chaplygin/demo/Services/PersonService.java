package com.by.chaplygin.demo.Services;

import com.by.chaplygin.demo.Enums.PersonRole;
import com.by.chaplygin.demo.Exceptions.PartyNotFoundException;
import com.by.chaplygin.demo.Exceptions.PersonNotFoundException;
import com.by.chaplygin.demo.Model.Party;
import com.by.chaplygin.demo.Model.Person;
import com.by.chaplygin.demo.Repositories.PartyRepository;
import com.by.chaplygin.demo.Repositories.PersonRepository;
import com.by.chaplygin.demo.Security.PersonDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonService implements UserDetailsService {
    private final PartyRepository partyRepository;
    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByUsername(s);

        if (person.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new PersonDetails(person.get());
    }

    public void addPartyToPersonList(int personId, int partyId) throws PersonNotFoundException, PartyNotFoundException {
        Optional<Person> person = personRepository.findById(personId);
            if(person.isEmpty()){
                throw new PersonNotFoundException("cannot find person");
            }
        Optional<Party> party = partyRepository.findById(partyId);
            if(party.isEmpty())
                throw new PartyNotFoundException("cannot find party");
        party.get().getGuests().add(person.get());

    }

    @Transactional
    public void deleteAccount(String username){
        Optional<Person> person = personRepository.findByUsername(username);
        personRepository.delete(person.get());
    }

    @Transactional
    public Person indexPage(String username) throws PersonNotFoundException {
        Optional<Person> person = personRepository.findByUsername(username);
        if(person.isEmpty()){
            throw new PersonNotFoundException("person not found");
        }
        return person.get();
    }


    public void updatePerson(String username, Person updatedPerson){
        Optional<Person> personToBeUpdated = personRepository.findByUsername(username);
        updatedPerson.setActive(personToBeUpdated.get().isActive());
        updatedPerson.setDateOfCreate(personToBeUpdated.get().getDateOfCreate());
        updatedPerson.setDateOfUpdate(LocalDateTime.now());

        // Создаем новую коллекцию и добавляем в нее все элементы из старой коллекции
        Set<Party> newAllPersonParty = new HashSet<>();
        newAllPersonParty.addAll(personToBeUpdated.get().getAllPersonParty());
        List<PersonRole> newPersonRoles = new ArrayList<>();
        newPersonRoles.addAll(personToBeUpdated.get().getRoles());
        updatedPerson.setRoles(newPersonRoles);
        // Устанавливаем новую коллекцию в новый объект Person
        updatedPerson.setAllPersonParty(newAllPersonParty);

        updatedPerson.setPassword(personToBeUpdated.get().getPassword());
        personRepository.save(updatedPerson);
    }



}
