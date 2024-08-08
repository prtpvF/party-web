package by.intexsoft.diplom.publicapi.service;

import by.intexsoft.diplom.common.model.PersonModel;
import by.intexsoft.diplom.common.repository.PersonRepository;
import by.intexsoft.diplom.publicapi.dto.PasswordResetDto;
import by.intexsoft.diplom.publicapi.dto.PersonDto;
import by.intexsoft.diplom.publicapi.dto.PersonUpdateDto;
import by.intexsoft.diplom.publicapi.exception.EmailIsTakenException;
import by.intexsoft.diplom.publicapi.exception.PasswordsDontMatchException;
import by.intexsoft.diplom.publicapi.exception.PersonNotFoundException;
import by.intexsoft.diplom.publicapi.exception.UsernameIsTakenException;
import by.intexsoft.diplom.publicapi.util.ObjectMapper;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {

        private final PersonRepository personRepository;
        private final PasswordEncoder passwordEncoder;
        private final ModelMapper modelMapper;
        private final ObjectMapper objectMapper;

        public PersonDto getPersonDto(String username) {
            PersonModel person = getPersonByUsername(username);
            return objectMapper.convertPersonToDto(person);
        }

        public void resetPassword(PasswordResetDto passwordResetDto, Principal principal) {
            String username = getusernameFromToken(principal);
            PersonModel person = getPersonByUsername(username);
            doPasswordsMatch(passwordResetDto.getOldPassword(), username);
            person.setPassword(passwordEncoder.encode(passwordResetDto.getNewPassword()));
            personRepository.save(person);
            log.info(username + " has just reset password");
        }

        public void deleteAccount(Principal principal) {
            String username = getusernameFromToken(principal);
            PersonModel person = getPersonByUsername(username);
            isPersonIdValid(person.getId());
            personRepository.deleteById(person.getId());
            log.info(username + " has just deleted his profile");
        }

        public void updateProfile(Principal principal,
                                  PersonUpdateDto personUpdateDto) {
            String username = getusernameFromToken(principal);
            PersonModel person = getPersonByUsername(username);
            isUpdatedInfoValid(personUpdateDto);
            modelMapper.map(personUpdateDto, person);
            personRepository.save(person);
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

        private void doPasswordsMatch(String oldPassword, String username) {
            Optional<PersonModel> person = personRepository.findByUsername(username);
            if (!passwordEncoder.matches(oldPassword, person.get().getPassword())) {
                throw new PasswordsDontMatchException("passwords don't match");
            }
        }

        private String getusernameFromToken(Principal principal){
            try{
                return principal.getName();
            } catch (SignatureVerificationException exception){
                log.warn("something wrong with token");
                return "something wrong with token";
            }
        }
}
