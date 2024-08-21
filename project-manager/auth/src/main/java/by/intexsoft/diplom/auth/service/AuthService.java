package by.intexsoft.diplom.auth.service;

import by.intexsoft.diplom.auth.dto.AuthResponseBuilder;
import by.intexsoft.diplom.auth.dto.LoginDto;
import by.intexsoft.diplom.auth.dto.RegistrationDto;
import by.intexsoft.diplom.auth.exception.InvalidDataException;
import by.intexsoft.diplom.common.model.enums.PersonRolesEnum;
import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.common.model.role.PersonRoleModel;
import by.intexsoft.diplom.common.repository.person.PersonRepository;
import by.intexsoft.diplom.common.repository.person.RoleRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpStatus.CREATED;

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

        @Value("${keycloak.auth-login-url}")
        private String keycloakAuthLoginUrl;

        @Value("${keycloak.client-id}")
        private String clientId;

        public HttpStatus register(RegistrationDto registrationDto) {
                keycloakService.saveUserIntoKeycloakDb(registrationDto);
                saveUserIntoApplicationDb(registrationDto);
            return CREATED;
        }

        public ResponseEntity<AuthResponseBuilder> login(LoginDto loginDto, HttpServletResponse response) {

                HttpEntity<MultiValueMap<String, String>> requestEntity = createAuthRequest(loginDto);
                ResponseEntity<AuthResponseBuilder> tokenResponse = restTemplate
                        .postForEntity(keycloakAuthLoginUrl,
                                requestEntity,
                                AuthResponseBuilder.class);

                log.info("Authorization request successful. Response: {}", tokenResponse.getStatusCode());

                return processAuthResponse(tokenResponse, response);

        }

        private HttpEntity<MultiValueMap<String, String>> createAuthRequest(LoginDto loginDto) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
            mapAdditionalFields(loginDto, multiValueMap);

            return new HttpEntity<>(multiValueMap, headers);
        }

        private ResponseEntity<AuthResponseBuilder> processAuthResponse(ResponseEntity<AuthResponseBuilder> tokenResponse,
                                                                        HttpServletResponse response) {
            AuthResponseBuilder authResponse = tokenResponse.getBody();
            if (authResponse != null) {
                cookieService.addRefreshTokenInCookie("refresh-token",
                        authResponse.getRefreshToken(),
                        response);

                return new ResponseEntity<>(authResponse, HttpStatus.OK);
            } else {
                throw new InvalidDataException("Authorization response body is null");
            }
        }

        private void saveUserIntoApplicationDb(RegistrationDto registrationDto) {
            PersonModel person = new PersonModel();
            modelMapper.map(registrationDto, person);
            person.setRole(getPersonRole(registrationDto.isOrganizer()));
            personRepository.save(person);
        }

        private void mapAdditionalFields(LoginDto loginDto,
                                         MultiValueMap<String,
                                                 String> multiValueMap) {

            multiValueMap.add("client_id", clientId);
            multiValueMap.add("grant_type", "password");
            multiValueMap.add("username", loginDto.getUsername());
            multiValueMap.add("password", loginDto.getPassword());
        }

        private PersonRoleModel getPersonRole(boolean organizer) {
            String roleName = organizer 
                    ? PersonRolesEnum.ORGANIZER.name()
                    : PersonRolesEnum.USER.name();
            return roleRepository.findByRoleName(roleName)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));
        }
}