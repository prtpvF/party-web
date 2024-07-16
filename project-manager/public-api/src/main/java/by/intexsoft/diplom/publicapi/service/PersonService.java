package by.intexsoft.diplom.publicapi.service;

import by.intexsoft.diplom.common_module.jwt.JwtUtil;
import by.intexsoft.diplom.common_module.model.Person;
import by.intexsoft.diplom.common_module.repository.PersonRepository;
import by.intexsoft.diplom.publicapi.dto.PasswordResetDto;
import by.intexsoft.diplom.publicapi.dto.PersonDto;
import by.intexsoft.diplom.publicapi.exception.PasswordsDontMatchException;
import by.intexsoft.diplom.publicapi.exception.PersonNotFoundException;
import by.intexsoft.diplom.publicapi.util.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(PersonService.class);


    public PersonDto getPersonDto(String username) {
        Person person = getPersonByUsername(username);
        return objectMapper.convertPersonToDto(person);
    }

    public void resetPassword(PasswordResetDto passwordResetDto, String token) {
        String username = jwtUtil.validateTokenAndRetrieveClaim(token);
        Person person = getPersonByUsername(username);
        doPasswordsMatch(passwordResetDto.getOldPassword(), username);
        person.setPassword(passwordEncoder.encode(passwordResetDto.getNewPassword()));
        personRepository.save(person);
        logger.warn(username + " has just reset password");
    }

    public void deleteAccount(String token) {
        String username = jwtUtil.validateTokenAndRetrieveClaim(token);
        Person person = getPersonByUsername(username);
        isPersonIdValid(person.getId());
        personRepository.deleteById(person.getId());
    }

    private Person getPersonByUsername(String username) {
        return personRepository.findByUsername(username)
                .orElseThrow(() -> new PersonNotFoundException("person with this username not found"));
    }

    private void isPersonIdValid(int id) {
        personRepository.findById(id).orElseThrow(()
                -> new PersonNotFoundException("person with this id not found"));
    }

    private void doPasswordsMatch(String oldPassword, String username) {
        Optional<Person> person = personRepository.findByUsername(username);
        if (!passwordEncoder.matches(oldPassword, person.get().getPassword())) {
            throw new PasswordsDontMatchException("passwords don't match");
        }
    }


}
