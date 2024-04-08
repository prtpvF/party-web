package com.by.chaplygin.demo.Services;

import com.by.chaplygin.demo.Enums.PersonRole;
import com.by.chaplygin.demo.Exceptions.PartyNotFoundException;
import com.by.chaplygin.demo.Exceptions.PersonIsBannedException;
import com.by.chaplygin.demo.Exceptions.PersonNotFoundException;
import com.by.chaplygin.demo.Exceptions.ReportAlreadyExistException;
import com.by.chaplygin.demo.Model.*;
import com.by.chaplygin.demo.Repositories.*;
import com.by.chaplygin.demo.Security.PersonDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PersonService implements UserDetailsService {
    private final PartyRepository partyRepository;
    private final PersonRepository personRepository;
    private final OrganizerRepository organizerRepository;
    private final RequestRepository requestRepository;
    private final BansRepository bansRepository;
    private final ReportRepository reportRepository;


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

    public void addPartyToPerson(String username, int id){   //todo test
        Optional<Person> person = personRepository.findByUsername(username);
        Optional<Party> party = partyRepository.findById(id);

        if(isPersonBanned(person.get().getId(), party.get().getOrganizer().getId())){
            ParticipationRequests participationRequests = new ParticipationRequests();
            participationRequests.setPartyId(party.get());
            participationRequests.setGuestId(person.get());
            person.get().getRequests().add(participationRequests);
            party.get().getRequests().add(participationRequests);
            requestRepository.save(participationRequests);
        }else
            try {
                throw new PersonIsBannedException("you are banned");
            } catch (PersonIsBannedException e) {

            }
    }

    public void convertPersonToOrganizer(String username) {
        Optional<Person> personOpt = personRepository.findByUsername(username);
        if (personOpt.isPresent()) {
            Person person = personOpt.get();
            Organizer organizer = new Organizer();
                BeanUtils.copyProperties(organizer, person);
                organizer.setDateOfCreate(LocalDateTime.now());
                organizerRepository.save(organizer);

        }
    }

    private boolean isPersonBanned(int personId, int organizerId){
        Optional<Person> person = getPersonById(personId);
        Optional<Organizer> organizer = getOrganizerById(organizerId);
        if (person.isPresent() && organizer.isPresent()) {
            Optional<Bans> ban = bansRepository.findBansByPersonAndOrganizer(person.get(), organizer.get());
            return ban.isPresent();
        } else {
            return false;
        }
    }

    private Optional<Person> getPersonById(int personId){
        Optional<Person> person = personRepository.findById(personId);
        return person;
    }

    private Optional<Organizer> getOrganizerById(int organizerId){
        Optional<Organizer> organizer = organizerRepository.findById(organizerId);
        return organizer;
    }

    public HttpStatus createReport(int personId, int partyId, String text){
        Optional<Person> person = personRepository.findById(personId);
        Optional<Party> party = partyRepository.findById(partyId);
        if(!isReportExist(personId,partyId)){
            Report report = new Report();
            report.setPerson(person.get());
            report.setParty(party.get());
            report.setText(text);
            SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy");
            String formattedDate = date.format(getCurrentDate());
            report.setDate(formattedDate);
            reportRepository.save(report);
        }
        else {
            try {
                throw new ReportAlreadyExistException("report already exists");
            } catch (ReportAlreadyExistException e) {
                return HttpStatus.FOUND;
            }
        }
        return HttpStatus.OK;
    }

    private Date getCurrentDate(){
        Date date = new Date();
        return date;
    }

    private boolean isReportExist(int personId, int partyId){
        Optional<Person> person = personRepository.findById(personId);
        Optional<Party> party= partyRepository.findById(partyId);
        if(reportRepository.findReportByPersonAndParty(person.get(),party.get()).isPresent()){
            return true;
        }
        return false;
    }





}
