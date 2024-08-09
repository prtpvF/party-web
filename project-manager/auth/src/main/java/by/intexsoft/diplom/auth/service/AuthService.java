package by.intexsoft.diplom.auth.service;

import by.intexsoft.diplom.auth.dto.RegistrationDto;
import by.intexsoft.diplom.common.model.PersonModel;
import by.intexsoft.diplom.common.model.enums.PersonRolesEnum;
import by.intexsoft.diplom.common.model.role.PersonRoleModel;
import by.intexsoft.diplom.common.repository.RoleRepository;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

        private final RoleRepository roleRepository;
        private final PasswordEncoder passwordEncoder;
        private final ModelMapper modelMapper;
        private final Keycloak keycloak;

        @Value("${keycloak.realm}")
        private String realm;

//        /**
//         * Method prepares person for registration, checks person's existing in db,
//         * sends email with verification code and starts threads which will delete person in case
//         * person won't finish registration process
//         * @param registrationDto - dto with necessary data for registration
//         */
//        @Transactional
//        public void register(RegistrationDto registrationDto) {
//            checkPersonExists(registrationDto.getUsername(), registrationDto.getEmail());
//            PersonModel personModel = convertDtoToPerson(registrationDto);
//            preparePersonForRegistration(personModel, registrationDto.isOrganizer());
//            personRepository.save(personModel);
//            log.info("New person has registered: {}", personModel);
//        }

        public Integer register(UserRepresentation userRepresentation, Boolean isOrganizer) {
            RealmResource realmResource = getCurrentRealm();
            userRepresentation.setEnabled(true);
            userRepresentation.setGroups(Collections.singletonList(setPersonGroup(isOrganizer)));
            userRepresentation.setEmailVerified(false);
            UsersResource usersResource = getUserResource();
            Response response = realmResource.users().create(userRepresentation);

                List<UserRepresentation> representations = usersResource
                        .searchByUsername(userRepresentation.getUsername(),
                                true);
                if(!CollectionUtil.isEmpty(representations)) {
                    UserRepresentation representation = representations.stream().filter(user ->
                                    Objects.equals(false, user.isEmailVerified()))
                            .findFirst().orElse(null);

                    log.info("Email was sent to user id: {}", representation.getId());
                    emailVerification(representation.getId());
                }

            return response.getStatus();
        }

        public void sendEmailVerification(String userId) {
            UsersResource userResource = getUserResource();
            userResource.get(userId).sendVerifyEmail();
        }

        private UsersResource getUserResource() {
            return keycloak.realm(realm).users();
        }

        private String setPersonGroup(Boolean isOrganizer) {
            if(isOrganizer) {
                return "ORGANIZER";
            }
            return "USER";
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

        private RealmResource getCurrentRealm() {
           return keycloak.realm(realm);
        }

        private UsersResource getUsersResource() {
            RealmResource realm1 = keycloak.realm(realm);
            return realm1.users();
        }

        public void emailVerification(String userId){

            UsersResource usersResource = getUsersResource();
            usersResource.get(userId).sendVerifyEmail();
        }
}