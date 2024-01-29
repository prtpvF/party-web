package com.by.chaplygin.demo.Services;

import com.by.chaplygin.demo.Dto.PersonDto;
import com.by.chaplygin.demo.Exceptions.PartyNotFoundException;
import com.by.chaplygin.demo.Exceptions.PersonNotFoundException;
import com.by.chaplygin.demo.Model.Party;
import com.by.chaplygin.demo.Model.Person;
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
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartyServices {
    private final PartyRepository partyRepository;
    private final PersonRepository personRepository;
    private final EntityManager entityManager;

    public void createParty(String username, Party party) throws PersonNotFoundException {
            Optional<Person> person = personRepository.findByUsername(username);
            if(!person.isPresent()){
                throw new PersonNotFoundException("Person doesnt exists");
            }

            party.setDateOfCreate(Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime());
            party.setOrganizer(person.get());
            person.get().setAllOrgParty(List.of(party));
        partyRepository.save(party);
    }

    public void deleteParty(Party party){
        partyRepository.delete(party);
    }

    public List<Party> findAllParty(){
        Session session = entityManager.unwrap(Session.class);
        List<Party> parties = session.createQuery("select p from Party p LEFT JOIN FETCH p.organizer ").getResultList();

        return parties;
    }

    public Party getParty(int id) throws PartyNotFoundException {
        Optional<Party> party = partyRepository.findById(id);
        if(!party.isPresent())
            throw new PartyNotFoundException("Party not found");
        else
            return party.get();
    }

}
