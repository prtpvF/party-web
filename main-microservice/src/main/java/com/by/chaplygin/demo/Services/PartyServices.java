package com.by.chaplygin.demo.Services;

import com.by.chaplygin.demo.Dto.PersonDto;
import com.by.chaplygin.demo.Exceptions.EmptyPartyListException;
import com.by.chaplygin.demo.Exceptions.PartyNotFoundException;
import com.by.chaplygin.demo.Exceptions.PersonNotFoundException;
import com.by.chaplygin.demo.Model.Organizer;
import com.by.chaplygin.demo.Model.Party;
import com.by.chaplygin.demo.Model.Person;
import com.by.chaplygin.demo.Repositories.OrganizerRepository;
import com.by.chaplygin.demo.Repositories.PartyRepository;
import com.by.chaplygin.demo.Repositories.PersonRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PartyServices {
    private final PartyRepository partyRepository;
    private final PersonRepository personRepository;
    private final EntityManager entityManager;
    private final OrganizerRepository organizerRepository;


    public void createParty(String username, Party party)  {
            Optional<Organizer> organizer = organizerRepository.findByUsername(username);

            party.setDateOfCreate(Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime());
            party.setOrganizer(organizer.get());
            organizer.get().getAllOrgParty().add(party);
        partyRepository.save(party);
    }

    public void deleteParty(int id){

        partyRepository.deleteById(id);
    }

    public List<Party> findAllParty(){
        List<Party> foundedParties = partyRepository.findAll();
        return foundedParties;
    }

    public Party getParty(int id) throws PartyNotFoundException {
        Optional<Party> party = partyRepository.findById(id);
        if(!party.isPresent())
            throw new PartyNotFoundException("Party not found");
        else
            return party.get();
    }

    public List<Party> findByProperty(String property, String value) {
        List<Party> foundedParties = new ArrayList<>();
        if ("name".equals(property)) {
            foundedParties = partyRepository.findByName(value);
        } else if ("address".equals(property)) {
            foundedParties = partyRepository.findByAddress(value);
        }

        if (foundedParties.isEmpty()) {
            throw new EmptyPartyListException("Cannot find parties");
        }
        return foundedParties;
    }

    //todo добавить логирование при пустом set
    public Set<Person> allPartyGuests(int id){
        Optional<Party> party = partyRepository.findById(id);
        Set<Person> guests = party.get().getGuests();
        if(guests.isEmpty())
            return  Collections.emptySet();
        return  guests;
    }





}
