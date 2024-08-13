package by.intexsoft.diplom.auth.service;

import by.intexsoft.diplom.auth.dto.RegistrationDto;
import by.intexsoft.diplom.auth.exception.PersonAlreadyExists;
import by.intexsoft.diplom.common.model.PersonModel;
import by.intexsoft.diplom.common.model.enums.PersonRolesEnum;
import by.intexsoft.diplom.common.model.role.PersonRoleModel;
import by.intexsoft.diplom.common.repository.PersonRepository;
import by.intexsoft.diplom.common.repository.RoleRepository;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

        private final RoleRepository roleRepository;
        private final PersonRepository personRepository;
        private final ModelMapper modelMapper;
        private final Keycloak keycloak;

        @Value("${keycloak.realm}")
        private String realm;

        public ResponseEntity register(UserRepresentation userRepresentation,
                                Boolean isOrganizer) {
            return saveUserIntoKeycloakDb(userRepresentation,
                    isOrganizer);
        }

        public void saveUserIntoApplicationDb(RegistrationDto registrationDto) {
            PersonModel person = new PersonModel();
            modelMapper.map(registrationDto, person);
            person.setRole(getPersonRole(registrationDto.isOrganizer()));
            personRepository.save(person);
        }

        public void sendEmailVerification(String userId) {
            UsersResource userResource = getUserResource();
            userResource.get(userId).sendVerifyEmail();
        }

        private ResponseEntity saveUserIntoKeycloakDb(UserRepresentation userRepresentation, Boolean isOrganizer) {
            RealmResource realmResource = getCurrentRealm();
            if(!isEmailTaken(userRepresentation.getEmail()) && !isUsernameTaken(userRepresentation.getUsername())) {
                userRepresentation.setEnabled(true);
                userRepresentation.setGroups(Collections.singletonList(setPersonGroup(isOrganizer)));
                userRepresentation.setEmailVerified(false);
                UsersResource usersResource = getUserResource();
                Response response = realmResource.users().create(userRepresentation);
                List<UserRepresentation> representations = usersResource
                        .searchByUsername(userRepresentation.getUsername(),
                                true);
                if (!CollectionUtil.isEmpty(representations)) {
                    UserRepresentation representation = representations.stream().filter(user ->
                                    Objects.equals(false, user.isEmailVerified()))
                            .findFirst().orElse(null);

                    log.info("Email was sent to user id: {}", representation.getId());
                    emailVerification(representation.getId());
                }
               return new ResponseEntity(response, HttpStatus.CREATED);
            }
            else {
               throw new PersonAlreadyExists("these credentials is already taken");
            }
        }

        private boolean isUsernameTaken(String username) {
            RealmResource realmResource = keycloak.realm(realm);
            List<UserRepresentation> users = realmResource.users().search(username, true);
            return !users.isEmpty();
        }

        private boolean isEmailTaken(String email) {
            RealmResource realmResource = keycloak.realm(realm);
            List<UserRepresentation> users = realmResource.users().search(null, null, null, email, 0, 1);
            return !users.isEmpty();
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

        public ResponseEntity<String> emailVerification(String userId) {
            UsersResource usersResource = getUsersResource();
            UserResource userResource = usersResource.get(userId);
            userResource.sendVerifyEmail();
            return new ResponseEntity<>(HttpStatus.OK);
        }
}
