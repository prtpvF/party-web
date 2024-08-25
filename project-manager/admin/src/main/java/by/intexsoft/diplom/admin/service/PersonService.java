package by.intexsoft.diplom.admin.service;

import by.intexsoft.diplom.admin.exception.PersonNotFoundException;
import by.intexsoft.diplom.common.model.enums.PersonRolesEnum;
import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.common.repository.person.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class PersonService {

        private final PersonRepository personRepository;

        public PersonModel findPersonByUsername(String username) {
            return personRepository.findByUsername(username)
                    .orElseThrow(() -> new PersonNotFoundException("Cannot find person with username " + username));
        }

        public void checkPersonRole(Principal principal) {
            PersonModel person = findPersonByUsername(principal.getName());
            if (!person.getRole().getRoleName().equals(PersonRolesEnum.ADMIN.name())) {
                throw new AccessDeniedException("You cannot enrich this functionality");
            }
        }

        public String getCityByPrincipal(Principal principal) {
            PersonModel person = findPersonByUsername(principal.getName());
            return person.getCity();
        }
}
