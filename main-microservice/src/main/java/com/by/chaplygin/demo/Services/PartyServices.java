package com.by.chaplygin.demo.Services;

import com.by.chaplygin.demo.Dto.PersonDto;
import com.by.chaplygin.demo.Exceptions.EmptyPartyListException;
import com.by.chaplygin.demo.Exceptions.PartyNotFoundException;
import com.by.chaplygin.demo.Exceptions.PersonNotFoundException;
import com.by.chaplygin.demo.Model.*;
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
    private final RequestsService requestsService;


    public void createParty(String username, Party party)  {
            Optional<Organizer> organizer = organizerRepository.findByUsername(username);

            party.setDateOfCreate(Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime());
            party.setOrganizer(organizer.get());
            organizer.get().getAllOrgParty().add(party);

        Party savedParty = partyRepository.save(party);
        sendEmailAboutPartyCreateInMyCity(savedParty);
    }

    public void deleteParty(int id){
        Optional<Party> party = partyRepository.findById(id);
        Set<Person> guests = party.get().getGuests();
        for (Person person:guests) {
            EmailParams emailParams = new EmailParams();
            emailParams.setEmail(person.getEmail());
            emailParams.setSubject("create");
            emailParams.setType(Type.DELETE_PARTY);
            requestsService.sendRequestToMailService(emailParams);
        }
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


    public void editParty(int id, Party updatedParty){
        Optional<Party> partyToBeUpdated = partyRepository.findById(id);
        updatedParty.setGuests(partyToBeUpdated.get().getGuests());
        updatedParty.setId(partyToBeUpdated.get().getId());
        updatedParty.setRequests(partyToBeUpdated.get().getRequests());
        updatedParty.setOrganizer(partyToBeUpdated.get().getOrganizer());
        partyRepository.save(updatedParty);

    }

    public Set<Party> getPartiesInMyCity(String city){
        Set<Party> parties= partyRepository.findAllByCity(city);
        if(parties.isEmpty())
            throw new EmptyPartyListException("no parties found in this city");
        if (!validateCityString(city))
            throw new IllegalArgumentException("the city was entered incorrectly");
        return parties;
    }

    private boolean validateCityString(String city) {
        // Проверяем, что строка не пустая и содержит хотя бы 2 символа
        if (city == null || city.length() < 2) {
            return false;
        }

        // Проверяем, что строка состоит только из букв (без цифр, пробелов и других символов)
        if (!city.matches("^[a-zA-Z]+$")) {
            return false;
        }

        return true;
    }

    private void sendEmailAboutPartyCreateInMyCity(Party party){
        Set<Person> personsInThisCity = personRepository.findAllByCity(party.getCity());
        if(!personsInThisCity.isEmpty()){


            for (Person person:personsInThisCity){
                EmailParams emailParams = new EmailParams();
                emailParams.setEmail(person.getEmail());
                emailParams.setSubject("create");
                emailParams.setMessage(generateUrl(party.getId()));
                requestsService.sendRequestToMailService(emailParams );
            }
        }
    }
    private String generateUrl(int partyId){
        String url = "192.168.100.5:8080/party/page/" + partyId;

        return url;
    }




}
