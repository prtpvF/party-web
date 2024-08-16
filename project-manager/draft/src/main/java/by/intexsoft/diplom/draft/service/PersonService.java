package by.intexsoft.diplom.draft.service;

import by.intexsoft.diplom.common.model.party.PartyDraftModel;
import by.intexsoft.diplom.common.model.party.PartyEntity;
import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.common.repository.person.PersonRepository;
import by.intexsoft.diplom.draft.exception.IllegalDraftOwnerException;
import by.intexsoft.diplom.draft.exception.IllegalPartyOwnerException;
import by.intexsoft.diplom.draft.exception.PersonNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class PersonService {

        private final PersonRepository personRepository;

        public PersonModel findByPrincipal(Principal principal) {
            return personRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new PersonNotFoundException("cannot find person"));
        }

        public PersonModel findById(Integer id) {
            return personRepository.findById(id)
                    .orElseThrow(() -> new PersonNotFoundException("cannot find person"));
        }

        public void verifyDraftOwnership(PersonModel authenticatedPerson,
                                         PartyDraftModel draft) {
            if (!draft.getOwner().equals(authenticatedPerson)) {
                throw new IllegalDraftOwnerException("You are not the owner of this draft!");
            }
        }

        public void verifyPartyOwnership(PartyEntity party, Principal principal) {
            PersonModel authenticatedPerson = findByPrincipal(principal);
            if (!party.getOrganizer().equals(authenticatedPerson)) {
                throw new IllegalPartyOwnerException("You are not the owner of this party!");
            }
        }
}