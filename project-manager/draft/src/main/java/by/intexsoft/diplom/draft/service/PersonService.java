package by.intexsoft.diplom.draft.service;

import by.intexsoft.diplom.common.model.party.PartyEntity;
import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.common.repository.person.PersonRepository;
import by.intexsoft.diplom.draft.exception.IllegalPartyOwnerException;
import by.intexsoft.diplom.draft.exception.PersonNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {

    private final PersonRepository personRepository;


        public PersonModel findByPrincipal(Principal principal) {
            PersonModel p = personRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new PersonNotFoundException("cannot find person"));
//            System.out.println(p);
//            System.out.println(p.getId());
            return p;
        }

        public PersonModel findById(Integer id) {
            return personRepository.findById(id)
                    .orElseThrow(() -> new PersonNotFoundException("cannot find person"));
        }

        public void verifyPartyOwnership(PartyEntity party, Principal principal) {
            PersonModel authenticatedPerson = findByPrincipal(principal);
            if (!party.getOrganizer().equals(authenticatedPerson)) {
                throw new IllegalPartyOwnerException("You are not the owner of this party!");
            }
        }
}