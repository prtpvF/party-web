package by.intexsoft.diplom.auth.service;

import by.intexsoft.diplom.auth.dto.PasswordResetDto;
import by.intexsoft.diplom.auth.dto.RegistrationDto;
import by.intexsoft.diplom.auth.exception.PersonAlreadyExists;
import by.intexsoft.diplom.auth.exception.PersonNotFoundException;
import by.intexsoft.diplom.common_module.jwt.JwtToken;
import by.intexsoft.diplom.common_module.models.Person;
import by.intexsoft.diplom.common_module.models.enums.PersonRolesEnum;
import by.intexsoft.diplom.common_module.models.roles.PersonRole;
import by.intexsoft.diplom.common_module.repository.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final PasswordEncoder passwordEncoder;
    private final JwtToken jwtToken;

    @Transactional
    public void register(RegistrationDto registrationDto) {
        checkPersonExists(registrationDto.getUsername(), registrationDto.getEmail());
        Person person = convertDtoToPerson(registrationDto);
        preparePersonForRegistration(person, registrationDto.isOrganizer());
        personRepository.save(person);
        logger.info("New person has registered: {}", person);
    }

    private void checkPersonExists(String username, String email) {
        personRepository.findByUsername(username).ifPresent(p -> {
            throw new PersonAlreadyExists("Person with this username already exists");
        });
        personRepository.findByEmail(email).ifPresent(p -> {
            throw new PersonAlreadyExists("Person with this email already exists");
        });
    }

    private Person convertDtoToPerson(RegistrationDto registrationDto) {
        return modelMapper.map(registrationDto, Person.class);
    }

    private void preparePersonForRegistration(Person person, boolean isOrganizer) {
        person.setActive(true);
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRating(0.0);
        person.setRole(definePersonRole(isOrganizer));
    }


    private PersonRole definePersonRole(boolean isOrg) {
        if (isOrg) {
            return new PersonRole(PersonRolesEnum.ORGANIZER.name());
        }
        return new PersonRole(PersonRolesEnum.USER.name());
    }

    private void isPersonExists(String username) {
        personRepository.findByUsername(username).ifPresent(p -> {
            new PersonNotFoundException("person with this username not found");
        });
    }


    public String createJwtToken(String username) {
        isPersonExists(username);
        String token = jwtToken.generateToken(username);
        return token;
    }








}
