package by.intexsoft.diplom.auth.service;

import by.intexsoft.diplom.auth.dto.AuthenticationDTOResponse;
import by.intexsoft.diplom.auth.exception.RefreshTokenException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

        private final ObjectMapper objectMapper;
        @Value("${keycloak.auth-login-url}")
        private String KEYCLOAK_AUTH_LOGIN;
        private final RestTemplate restTemplate;

        @Value("${keycloak.client-id}")
        private String clientId;

        public ResponseEntity<?> refreshToken(Cookie cookie) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "refresh_token");
            body.add("client_id", clientId);
            body.add("refresh_token", cookie.getValue());

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<?> response = restTemplate.postForEntity(KEYCLOAK_AUTH_LOGIN, requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                try {
                    JsonNode jsonNode = objectMapper.readTree(Objects.requireNonNull(response.getBody()).toString());
                    String accessToken = jsonNode.get("access_token").asText();
                    return new ResponseEntity<>(new AuthenticationDTOResponse(accessToken), HttpStatus.OK);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new RefreshTokenException("Failed to refresh token");
            }
        }
}
