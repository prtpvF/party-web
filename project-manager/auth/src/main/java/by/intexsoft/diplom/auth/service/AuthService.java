package by.intexsoft.diplom.auth.service;

import by.intexsoft.diplom.auth.dto.LogInDto;
import by.intexsoft.diplom.auth.dto.RegistrationDto;
import by.intexsoft.diplom.auth.exception.PersonAlreadyExists;
import by.intexsoft.diplom.auth.exception.PersonNotFoundException;
import by.intexsoft.diplom.common_module.jwt.JwtUtil;
import by.intexsoft.diplom.common_module.model.Person;
import by.intexsoft.diplom.common_module.model.enums.PersonRolesEnum;
import by.intexsoft.diplom.common_module.model.role.PersonRole;
import by.intexsoft.diplom.common_module.repository.PersonRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionImplementor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtToken;
    private final EntityManager entityManager;

    @Transactional
    public void register(RegistrationDto registrationDto) {
        checkPersonExists(registrationDto.getUsername(), registrationDto.getEmail());
        Person person = convertDtoToPerson(registrationDto);
        preparePersonForRegistration(person, registrationDto.isOrganizer());
        personRepository.save(person);
        logger.info("New person has registered: {}", person);
    }

    private void checkPersonExists(String username, String email) {
        personRepository.findByUsernameOrEmail(username, email).ifPresent(p -> {
            throw new PersonAlreadyExists("Person with this username already exists");
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


    private String createJwtToken(String username) {
        String token = jwtToken.generateToken(username);
        return token;
    }

    public String login(LogInDto logInDto) {
        Person person = personRepository.findByUsername(logInDto.getUsername())
                .filter(p -> passwordEncoder.matches(logInDto.getPassword(), p.getPassword()))
                .orElseThrow(() -> new PersonNotFoundException("Person with these credentials not found"));
        return createJwtToken(person.getUsername());
    }


}








