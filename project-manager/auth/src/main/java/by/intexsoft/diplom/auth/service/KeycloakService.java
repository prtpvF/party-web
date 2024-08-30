package by.intexsoft.diplom.auth.service;

import by.intexsoft.diplom.auth.exception.PersonAlreadyExists;
import by.intexsoft.diplom.common.repository.person.RoleRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.UserSessionRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakService {

        private final Keycloak keycloak;

        @Value("${keycloak.realm}")
        private String realm;

        public HttpStatus saveUserIntoKeycloakDb(UserRepresentation userRepresentation) {
            RealmResource realmResource = getCurrentRealm();
            if(!isEmailTaken(userRepresentation.getEmail()) &&
                    !isUsernameTaken(userRepresentation.getUsername())) {

                UsersResource usersResource = getUserResource();

                Response response = realmResource.users().create(userRepresentation);
                log.info("Response from Keycloak: {}", response.getStatus());


                List<UserRepresentation> representations = usersResource
                        .search(userRepresentation.getUsername(),
                                true);

                if (!representations.isEmpty()) {
                    UserRepresentation foundedRepresentation = representations.stream()
                            .filter(user -> Objects.equals(false,
                                            user.isEmailVerified()))
                            .findFirst().orElse(null);

                    emailVerification(foundedRepresentation.getId());
                    log.info("Email was sent to user id: {}", foundedRepresentation.getId());
               }

                return HttpStatus.valueOf(response.getStatus());
            }
            else {
                throw new PersonAlreadyExists("these credentials is already taken");
            }
        }

        public HttpStatus logoutFromKeycloak(String username,
                                             HttpServletRequest request) {
            String token = request.getHeader("Authorization").substring(7);
            System.out.println(token);
            revokeToken(token);
            RealmResource realmResource = getCurrentRealm();
            List<UserRepresentation> users = realmResource.users().list();

            for (UserRepresentation user : users) {
                if (user.getUsername().equals(username)) {
                    realmResource.users().get(user.getId()).logout();
                    return OK;
                }
            }
            throw new RuntimeException("User not found: " + username);
        }


        public void sendEmailVerification(String userId) {
            UsersResource userResource = getUserResource();
            userResource.get(userId).sendVerifyEmail();
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

        private RealmResource getCurrentRealm() {
            return keycloak.realm(realm);
        }

        private ResponseEntity<String> emailVerification(String userId) {
            UsersResource usersResource = getUsersResource();
            UserResource userResource = usersResource.get(userId);
            userResource.sendVerifyEmail();
            return new ResponseEntity<>(OK);
        }

        private UsersResource getUsersResource() {
            RealmResource realm1 = keycloak.realm(realm);
            return realm1.users();
        }

        private UsersResource getUserResource() {
            return keycloak.realm(realm).users();
        }

        private void revokeToken(String token) {
            keycloak.tokenManager().invalidate(token);
        }
}