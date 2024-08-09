package by.intexsoft.diplom.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Service was created for start thread for deleting person which haven't finished
 * email verification process.
 * Method deletes users with unverified emails from keycloak db
 * @version 2.0
 * @author Mihail Chaplygin
 */
@Service
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {

        private final Keycloak keycloak;

        @Value("${keycloak.realm}")
        private String realm;

        @Scheduled(initialDelay = 5000, fixedDelay = 60000)
        public void scheduleUserDeletion() {
             CompletableFuture.runAsync(() -> {
                 RealmResource realmResource = keycloak.realm(realm);

                 UsersResource userResource = realmResource.users();
                 List<UserRepresentation> representations = userResource.list();

                 for (UserRepresentation representation : representations) {
                     if(!representation.isEmailVerified()){
                         deleteUnverifiedUser(representation);
                     }
                 }
                     log.info("start cleaning unavailable users");

            });
        }

        private void deleteUnverifiedUser(UserRepresentation user) {
            RealmResource realmResource = keycloak.realm(realm);
            realmResource.users().delete(user.getId());
        }
}
