package by.intexsoft.diplom.auth.service;

import by.intexsoft.diplom.auth.dto.AuthenticationDTOResponse;
import by.intexsoft.diplom.auth.dto.LoginDto;
import by.intexsoft.diplom.auth.dto.RegistrationDto;
import by.intexsoft.diplom.auth.exception.InvalidDataException;
import by.intexsoft.diplom.auth.exception.PersonAlreadyExists;
import by.intexsoft.diplom.auth.exception.PersonNotFoundException;
import by.intexsoft.diplom.common.model.enums.PersonRolesEnum;
import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.common.model.role.PersonRoleModel;
import by.intexsoft.diplom.common.repository.person.PersonRepository;
import by.intexsoft.diplom.common.repository.person.RoleRepository;
import jakarta.transaction.Transactional;
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
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.CREATED;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

        private final RoleRepository roleRepository;
        private final PersonRepository personRepository;
        private final ModelMapper modelMapper;
        private final Keycloak keycloak;
        private final RestTemplate restTemplate;


        private String KEYCLOAK_AUTH_LOGIN = "http://localhost:8080/realms/free-party/protocol/openid-connect/token";

        @Value("${keycloak.client-id}")
        private String clientId;

        @Value("${keycloak.realm}")
        private String realm;

        public HttpStatus register(RegistrationDto registrationDto) {
                saveUserIntoKeycloakDb(registrationDto);
                saveUserIntoApplicationDb(registrationDto);
            return CREATED;
        }

        public AuthenticationDTOResponse login(LoginDto loginDto) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
            mapAdditionalFields(loginDto, multiValueMap);
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(multiValueMap, headers);

            try {
                ResponseEntity<?> response = restTemplate.postForEntity(KEYCLOAK_AUTH_LOGIN,
                        httpEntity,
                        Object.class);
                log.info("Authorization request successful. Response: {}", response);
                return new AuthenticationDTOResponse(response.getBody());
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                log.error("Authorization request failed with status code {} and response body: {}",
                        e.getStatusCode(),
                        e.getResponseBodyAsString(), e);
                throw new InvalidDataException("Authorization request failed");
            }
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

        private void mapAdditionalFields(LoginDto loginDto, MultiValueMap<String, String> multiValueMap) {
            multiValueMap.add("client_id", clientId);
            multiValueMap.add("grant_type", "password");
            multiValueMap.add("username", loginDto.getUsername());
            multiValueMap.add("password", loginDto.getPassword());
        }

        private HttpStatus saveUserIntoKeycloakDb(RegistrationDto registrationDto) {
            RealmResource realmResource = getCurrentRealm();
            if(!isEmailTaken(registrationDto.getEmail()) && !isUsernameTaken(registrationDto.getUsername())) {
                registrationDto.setEnabled(true);
                registrationDto.setGroups(Collections.singletonList(setPersonGroup(registrationDto.isOrganizer())));
                registrationDto.setEmailVerified(false);
                UsersResource usersResource = getUserResource();
                UserRepresentation representation1 = convertToUserRepresentation(registrationDto);

                System.out.println(representation1.getCredentials());
                realmResource.users().create(representation1);
                List<UserRepresentation> representations = usersResource
                        .searchByUsername(registrationDto.getUsername(),
                                true);
                if (!CollectionUtil.isEmpty(representations)) {
                    UserRepresentation representation = representations.stream().filter(user ->
                                    Objects.equals(false, user.isEmailVerified()))
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

        private UsersResource getUserResource() {
                return keycloak.realm(realm).users();
            }

        private String setPersonGroup(Boolean isOrganizer) {
            if(isOrganizer) {
                return PersonRolesEnum.ORGANIZER.toString();
            }
            return PersonRolesEnum.USER.toString();
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
                        .singletonList(String.valueOf(roleRepository
                                .findByRoleName(PersonRolesEnum
                                        .ORGANIZER.name()))));
            }
            userRepresentation.setGroups(registrationDto.getGroups());
            userRepresentation.setEmailVerified(false);
            userRepresentation.setCredentials(registrationDto.getCredentials());
            return userRepresentation;
        }
}