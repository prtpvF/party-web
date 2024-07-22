package by.intexsoft.diplom.auth.service;

import by.intexsoft.diplom.auth.dto.LogInDto;
import by.intexsoft.diplom.auth.dto.RegistrationDto;
import by.intexsoft.diplom.auth.exception.PersonAlreadyExists;
import by.intexsoft.diplom.auth.exception.PersonNotFoundException;
import by.intexsoft.diplom.common_module.model.Person;
import by.intexsoft.diplom.common_module.model.enums.PersonRolesEnum;
import by.intexsoft.diplom.common_module.model.role.PersonRole;
import by.intexsoft.diplom.common_module.repository.PersonRepository;
import by.intexsoft.diplom.common_module.repository.RoleRepository;
import by.intexsoft.diplom.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;


    @Transactional
    public void register(RegistrationDto registrationDto) {
        checkPersonExists(registrationDto.getUsername(), registrationDto.getEmail());
        Person person = convertDtoToPerson(registrationDto);
        preparePersonForRegistration(person, registrationDto.isOrganizer());
        personRepository.save(person);
        log.info("New person has registered: {}", person);
    }

    public String login(LogInDto logInDto) {
        Person person = personRepository.findByUsername(logInDto.getUsername())
                .filter(p -> passwordEncoder.matches(logInDto.getPassword(), p.getPassword()))
                .orElseThrow(() -> new PersonNotFoundException("Person with these credentials not found"));
        return createJwtToken(person.getUsername());
    }

    public void logout(HttpServletRequest request, HttpServletResponse response, String token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            jwtUtil.removeToken(getUsernameFromToken(token));
        }
    }

    private void checkPersonExists(String username, String email) {
        personRepository.findByUsernameOrEmail(username, email).ifPresent(p -> {
            throw new PersonAlreadyExists("Person with this username already exists");
        });
    }

    private Person convertDtoToPerson(RegistrationDto registrationDto) {
        return modelMapper.map(registrationDto, Person.class);
    }

    private void preparePersonForRegistration(Person person, boolean organizer) {
        person.setActive(true);
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRating(0.0);
        person.setRole(getPersonRole(organizer));
    }

    private PersonRole getPersonRole(boolean organizer) {
        String roleName = organizer ? PersonRolesEnum.ORGANIZER.name() : PersonRolesEnum.USER.name();
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));
    }


    private String createJwtToken(String username) {
        String token = jwtUtil.generateToken(username);
        return token;
    }

    private String getUsernameFromToken(String token) {
        return jwtUtil.validateTokenAndRetrieveClaim(token);
    }


}








