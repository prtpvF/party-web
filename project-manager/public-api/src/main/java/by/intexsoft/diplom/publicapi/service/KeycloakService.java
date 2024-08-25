package by.intexsoft.diplom.publicapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakService {

        private final Keycloak keycloak;

        @Value("${keycloak.realm}")
        private String realm;

        public void updateUserInKeycloakDb(String username, UserRepresentation updatedUser) {
            try {
                UsersResource usersResource = getUsersResource();
                List<UserRepresentation> users = usersResource.search(username, true);

                if (users.isEmpty()) {
                    log.warn("User with username {} not found in Keycloak", username);
                    throw new RuntimeException("User not found in Keycloak");
                }

                UserRepresentation userRepresentation = users.get(0);
                String userId = userRepresentation.getId();
                isEmailTaken(updatedUser.getUsername());
                isUsernameTaken(updatedUser.getUsername());
                UserResource userResource = usersResource.get(userId);

                mapAdditionalFields(updatedUser, userRepresentation);

                userResource.update(userRepresentation);
                emailVerification(userId);
                log.info("User with username {} successfully updated in Keycloak", username);
            } catch (Exception e) {
                log.error("Failed to update user with username {} in Keycloak", username, e);
                throw new RuntimeException("Failed to update user in Keycloak", e);
            }
        }

        private static void mapAdditionalFields(UserRepresentation updatedUser,
                                                UserRepresentation userRepresentation) {
            userRepresentation.setEmail(updatedUser.getEmail());
            userRepresentation.setFirstName(updatedUser.getFirstName());
            userRepresentation.setLastName(updatedUser.getLastName());
            userRepresentation.setEmailVerified(false);
            userRepresentation.setCredentials(updatedUser.getCredentials());
        }

        private boolean isUsernameTaken(String username) {
            RealmResource realmResource = keycloak.realm(realm);
            List<UserRepresentation> users = realmResource.users().search(username, true);
            return !users.isEmpty();
        }

        private boolean isEmailTaken(String email) {
            RealmResource realmResource = keycloak.realm(realm);
            List<UserRepresentation> users = realmResource.users().search(null,
                    null,
                    null,
                    email,
                    0,
                    1);
            return !users.isEmpty();
        }

        private UsersResource getUsersResource() {
            return keycloak.realm(realm).users();
        }

        private ResponseEntity<String> emailVerification(String id) {
            UsersResource usersResource = getUsersResource();
            UserResource userResource = usersResource.get(id);
            userResource.sendVerifyEmail();
            return new ResponseEntity<>(HttpStatus.OK);
        }
}