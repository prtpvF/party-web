package by.intexsoft.diplom.publicapi.service;

import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.common.repository.person.PersonRepository;
import by.intexsoft.diplom.publicapi.dto.PersonDto;
import by.intexsoft.diplom.publicapi.dto.PersonUpdateDto;
import by.intexsoft.diplom.publicapi.dto.UpdateRequestDto;
import by.intexsoft.diplom.publicapi.exception.EmailIsTakenException;
import by.intexsoft.diplom.publicapi.exception.PersonNotFoundException;
import by.intexsoft.diplom.publicapi.exception.UsernameIsTakenException;
import by.intexsoft.diplom.publicapi.util.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {

        private final PersonRepository personRepository;
        private final ModelMapper modelMapper;
        private final ObjectMapper objectMapper;
        private final KeycloakService keycloakService;

        public PersonDto getPersonDto(String username) {
            PersonModel person = getPersonByUsername(username);
            return objectMapper.convertPersonToDto(person);
        }

        public void deleteAccount(Principal principal) {
            PersonModel person = getPersonByPrincipal(principal);
            isPersonIdValid(person.getId());
            personRepository.deleteById(person.getId());
            log.info(principal.getName() + " has just deleted his profile");
        }

        public void updateProfile(Principal principal,
                                  UpdateRequestDto updateRequestDto) {
            PersonModel person = getPersonByPrincipal(principal);
            isUpdatedInfoValid(updateRequestDto.getPersonUpdateDto());
            modelMapper.map(updateRequestDto.getPersonUpdateDto(), person);
            keycloakService.updateUserInKeycloakDb(person.getUsername(), updateRequestDto.getRepresentation());
            personRepository.save(person);
        }

        public PersonModel getPersonByPrincipal(Principal principal){
            return getPersonByUsername( principal.getName());
        }

        private void isUpdatedInfoValid(PersonUpdateDto personUpdateDto) {
            isEmailTaken(personUpdateDto.getEmail());
            isUsernameTaken(personUpdateDto.getUsername());
        }

        private void isUsernameTaken(String username) {
            Optional<PersonModel> person = personRepository.findByUsername(username);
            if (person.isPresent()) {
                throw new UsernameIsTakenException("this username is taken!");
            }
        }

        private void isEmailTaken(String email) {
            Optional<PersonModel> person = personRepository.findByEmail(email);
            if (person.isPresent()) {
                throw new EmailIsTakenException("this email is taken!");
            }
        }

        private PersonModel getPersonByUsername(String username) {
            return personRepository.findByUsername(username)
                    .orElseThrow(() -> new PersonNotFoundException("person with this username not found"));
        }

        private void isPersonIdValid(int id) {
            personRepository.findById(id).orElseThrow(()
                    -> new PersonNotFoundException("person with this id not found"));
        }
}
