package by.intexsoft.diplom.chat.service;

import by.intexsoft.diplom.chat.exception.PersonNotFoundException;
import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.common.repository.person.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonService {

        private final PersonRepository personRepository;

        public PersonModel getPersonByUsername(String username) {
            return personRepository
                    .findByUsername(username)
                    .orElseThrow(() -> new PersonNotFoundException(
                            "cannot find person with this username"));
        }
}
