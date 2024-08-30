package by.intexsoft.diplom.auth.service;

import by.intexsoft.diplom.auth.authentication.PersonDetails;
import by.intexsoft.diplom.auth.dto.AuthResponseBuilder;
import by.intexsoft.diplom.auth.dto.LoginDto;
import by.intexsoft.diplom.auth.dto.RegistrationDto;
import by.intexsoft.diplom.auth.exception.InvalidDataException;
import by.intexsoft.diplom.auth.exception.PersonAlreadyExists;
import by.intexsoft.diplom.common.model.enums.PersonRolesEnum;
import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.common.model.role.PersonRoleModel;
import by.intexsoft.diplom.common.repository.person.PersonRepository;
import by.intexsoft.diplom.common.repository.person.RoleRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

        private final RoleRepository roleRepository;
        private final PersonRepository personRepository;
        private final ModelMapper modelMapper;
        private final RestTemplate restTemplate;
        private final CookieService cookieService;
        private final KeycloakService keycloakService;
        private final AuthRedisService authRedisService;

        @Value("${keycloak.auth-login-url}")
        private String keycloakAuthLoginUri;

        @Value("${keycloak.client-id}")
        private String clientId;

        @Value("${keycloak.grant-type}")
        private String grantType;

        public HttpStatus register(RegistrationDto registrationDto) {
            saveUserIntoApplicationDb(registrationDto);
             keycloakService.saveUserIntoKeycloakDb(
                    convertRegistrationDtoToRepresentation(
                            registrationDto));
             return HttpStatus.CREATED;
        }

        public ResponseEntity<AuthResponseBuilder> login(LoginDto loginDto,
                                                         HttpServletResponse response) {
            authRedisService.removeAllTokensFromRedis(loginDto.getUsername());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
            mapAdditionalFields(loginDto, multiValueMap);

            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(multiValueMap, headers);

            try {
                ResponseEntity<AuthResponseBuilder> tokenResponse = restTemplate.postForEntity(
                        keycloakAuthLoginUri,
                        httpEntity,
                        AuthResponseBuilder.class
                );
                log.info("Authorization request successful. Response: {}", response);

                AuthResponseBuilder authResponse = tokenResponse.getBody();
                if (authResponse != null) {

                    cookieService.addRefreshTokenInCookie("refresh-token",
                            authResponse.getRefreshToken(),
                            response);
                    authRedisService.addRefreshTokenIntoRedis(authResponse.getRefreshToken(),
                            loginDto.getUsername());
                    authRedisService.addAccessTokenIntoRedis(authResponse.getAccessToken(),
                            loginDto.getUsername());
                    return new ResponseEntity<>(authResponse, HttpStatus.OK);
                } else {
                    throw new InvalidDataException("Authorization response body is null");
                }
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                log.error("Authorization request failed with status code {} and response body: {}",
                        e.getStatusCode(),
                        e.getResponseBodyAsString(), e);
                throw new InvalidDataException("Authorization request failed");
            }
        }

        public HttpStatus logout(Principal principal,
                                 HttpServletRequest request) {
            if(principal != null) {
                authRedisService.isAccessTokenValid(principal.getName());
                String username = principal.getName();
                keycloakService.logoutFromKeycloak(username, request);
                authRedisService.removeAllTokensFromRedis(username);
                return HttpStatus.OK;
            }
            else throw new InvalidDataException("Principal is null");
        }

        private void saveUserIntoApplicationDb(RegistrationDto registrationDto) {
            isRegistrationDataValid(registrationDto);
            PersonModel person = new PersonModel();
            modelMapper.map(registrationDto, person);
            person.setRole(getPersonRole(registrationDto.isOrganizer()));
            personRepository.save(person);
        }

        private void mapAdditionalFields(LoginDto loginDto,
                                         MultiValueMap<String,
                                                 String> multiValueMap) {
            multiValueMap.add("client_id", clientId);
            multiValueMap.add("grant_type", grantType);
            multiValueMap.add("username", loginDto.getUsername());
            multiValueMap.add("password", loginDto.getPassword());
        }

        private PersonRoleModel getPersonRole(boolean organizer) {
            String roleName = organizer ? PersonRolesEnum.ORGANIZER.name() : PersonRolesEnum.USER.name();
            return roleRepository.findByRoleName(roleName)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));
        }

        private UserRepresentation convertRegistrationDtoToRepresentation(RegistrationDto registrationDto) {
            UserRepresentation representation = new UserRepresentation();
            String username = registrationDto.getUsername();
            String password = registrationDto.getPassword();
            String email = registrationDto.getEmail();

            representation.setUsername(username);
            representation.setEmail(email);
            representation.setEnabled(true);
            representation.setCredentials(createCredentialsRepresentation(password));
        return representation;
        }

        private List<CredentialRepresentation> createCredentialsRepresentation(String password) {
            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setValue(password);
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(password);
            return List.of(credentialRepresentation);
        }

        private void isRegistrationDataValid(RegistrationDto registrationDto) {

            if( personRepository.findByUsernameOrEmail(
                    registrationDto.getUsername(),
                    registrationDto.getEmail()).isPresent()) {
                throw new PersonAlreadyExists(
                        "person with this credentials already exist");
            }
        }
}