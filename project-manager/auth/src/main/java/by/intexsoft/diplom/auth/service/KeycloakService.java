package by.intexsoft.diplom.auth.service;

import by.intexsoft.diplom.auth.dto.RegistrationDto;
import by.intexsoft.diplom.auth.exception.PersonAlreadyExists;
import by.intexsoft.diplom.common.model.enums.PersonRolesEnum;
import by.intexsoft.diplom.common.repository.person.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.CREATED;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakService {

        private final Keycloak keycloak;
        private final RoleRepository repository;

        @Value("${keycloak.realm}")
        private String realm;

        public HttpStatus saveUserIntoKeycloakDb(RegistrationDto registrationDto) {
            RealmResource realmResource = getCurrentRealm();
            if(!isEmailTaken(registrationDto.getEmail()) &&
                    !isUsernameTaken(registrationDto.getUsername())) {

                mapAdditionalFieldToDto(registrationDto);
                UsersResource usersResource = getUserResource();
                UserRepresentation representation1 = convertToUserRepresentation(registrationDto);

                System.out.println(representation1.getCredentials());
                realmResource.users().create(representation1);
                List<UserRepresentation> representations = usersResource
                        .searchByUsername(registrationDto.getUsername(),
                                true);
                if (!CollectionUtil.isEmpty(representations)) {
                    UserRepresentation representation = representations.stream()
                            .filter(user -> Objects.equals(false,
                                            user.isEmailVerified()))
                            .findFirst().orElse(null);

                    emailVerification(representation.getId());
                    log.info("Email was sent to user id: {}", representation.getId());
                }
                return CREATED;
            }
            else {
                throw new PersonAlreadyExists("these credentials is already taken");
            }
        }

        public void sendEmailVerification(String userId) {
            UsersResource userResource = getUserResource();
            userResource.get(userId).sendVerifyEmail();
        }

        public boolean isUsernameTaken(String username) {
            RealmResource realmResource = keycloak.realm(realm);
            List<UserRepresentation> users = realmResource.users().search(username, true);
            return !users.isEmpty();
        }

        public boolean isEmailTaken(String email) {
            RealmResource realmResource = keycloak.realm(realm);
            List<UserRepresentation> users = realmResource.users().search(null,
                    null,
                    null,
                    email,
                    0,
                    1);
            return !users.isEmpty();
        }

        private void mapAdditionalFieldToDto(RegistrationDto registrationDto) {
            registrationDto.setEnabled(true);
            registrationDto.setGroups(Collections.singletonList(setPersonGroup(registrationDto.isOrganizer())));
            registrationDto.setEmailVerified(false);
        }

        private String setPersonGroup(Boolean isOrganizer) {
            if(isOrganizer) {
                return PersonRolesEnum.ORGANIZER.toString();
            }
            return PersonRolesEnum.USER.toString();
        }

        private RealmResource getCurrentRealm() {
            return keycloak.realm(realm);
        }

        private ResponseEntity<String> emailVerification(String userId) {
            UsersResource usersResource = getUsersResource();
            UserResource userResource = usersResource.get(userId);
            userResource.sendVerifyEmail();
            return new ResponseEntity<>(HttpStatus.OK);
        }

        private UserRepresentation convertToUserRepresentation(RegistrationDto registrationDto) {
            UserRepresentation userRepresentation = new UserRepresentation();
            userRepresentation.setUsername(registrationDto.getUsername());
            userRepresentation.setEmail(registrationDto.getEmail());
            userRepresentation.setEnabled(registrationDto.isEnabled());
            if(registrationDto.isOrganizer()) {
                userRepresentation.setRealmRoles(Collections
                        .singletonList(String.valueOf(repository
                                .findByRoleName(PersonRolesEnum
                                        .ORGANIZER.name()))));
            }
            userRepresentation.setGroups(registrationDto.getGroups());
            userRepresentation.setEmailVerified(false);
            userRepresentation.setCredentials(registrationDto.getCredentials());
            return userRepresentation;
        }

        private UsersResource getUsersResource() {
            RealmResource realm1 = keycloak.realm(realm);
            return realm1.users();
        }

        private UsersResource getUserResource() {
            return keycloak.realm(realm).users();
        }
}