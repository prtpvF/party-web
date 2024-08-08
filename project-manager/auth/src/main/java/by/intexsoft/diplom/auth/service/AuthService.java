package by.intexsoft.diplom.auth.service;

import by.intexsoft.diplom.auth.dto.RegistrationDto;
import by.intexsoft.diplom.auth.exception.PersonAlreadyExists;
import by.intexsoft.diplom.common.model.PersonModel;
import by.intexsoft.diplom.common.model.enums.PersonRolesEnum;
import by.intexsoft.diplom.common.model.role.PersonRoleModel;
import by.intexsoft.diplom.common.repository.PersonRepository;
import by.intexsoft.diplom.common.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

        private final PersonRepository personRepository;
        private final RoleRepository roleRepository;
        private final PasswordEncoder passwordEncoder;
        private final ModelMapper modelMapper;

        /**
         * Method prepares person for registration, checks person's existing in db,
         * sends email with verification code and starts threads which will delete person in case
         * person won't finish registration process
         * @param registrationDto - dto with necessary data for registration
         */
        @Transactional
        public void register(RegistrationDto registrationDto) {
            checkPersonExists(registrationDto.getUsername(), registrationDto.getEmail());
            PersonModel personModel = convertDtoToPerson(registrationDto);
            preparePersonForRegistration(personModel, registrationDto.isOrganizer());
            personRepository.save(personModel);
            log.info("New person has registered: {}", personModel);
        }


        private void checkPersonExists(String username, String email) {
            personRepository.findByUsernameOrEmail(username, email).ifPresent(p -> {
                throw new PersonAlreadyExists("Person with this credentials already exists");
            });
        }

        private PersonModel convertDtoToPerson(RegistrationDto registrationDto) {
            return modelMapper.map(registrationDto, PersonModel.class);
        }

        private void preparePersonForRegistration(PersonModel personModel,
                                                  boolean organizer) {
            personModel.setActive(true);
            personModel.setPassword(passwordEncoder.encode(personModel.getPassword()));
            personModel.setRating(0.0);
            personModel.setRole(getPersonRole(organizer));
        }

        private PersonRoleModel getPersonRole(boolean organizer) {
            String roleName = organizer ? PersonRolesEnum.ORGANIZER.name() : PersonRolesEnum.USER.name();
            return roleRepository.findByRoleName(roleName)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));
        }
}