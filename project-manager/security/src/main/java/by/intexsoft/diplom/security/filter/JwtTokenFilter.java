package by.intexsoft.diplom.security.filter;

import by.intexsoft.diplom.security.service.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

        private final RedisService redisService;

//        @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
        private String issuerUri = "http://10.104.10.23:7777/realms/free-party";

        private static final String[] ENDPOINTS = {
                "/auth/registration",
                "/auth/login",
                "/swagger-ui/*",
                "/public/person/find",
                "/v3/api-docs/*",
        };

        @Override
        protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain filterChain) throws ServletException, IOException {
            String requestUri = request.getRequestURI();
            if(!isEndpointOpen(requestUri)) {
                String authHeader = request.getHeader("Authorization");
                String accessToken = authHeader.substring(7);
                String username = getLoginFromJwt(accessToken);
                String value = redisService.isAccessTokenValid(username);
                if (value != null) {
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
            else {
                filterChain.doFilter(request, response);
            }
            filterChain.doFilter(request, response);
        }

        private String getLoginFromJwt(String token) {
            JwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(issuerUri);
            Jwt jwt = jwtDecoder.decode(token);
            return jwt.getClaimAsString("preferred_username");
        }

        private boolean isEndpointOpen(String requestURI) {
            return matchesAny(requestURI, ENDPOINTS);
        }

        private boolean matchesAny(String requestURI, String[] endpoints) {
            for (String endpoint : endpoints) {
                if (requestURI.matches(convertToRegex(endpoint))) {
                    return true;
                }
            }
            return false;
        }

        private String convertToRegex(String endpoint) {
            return endpoint.replaceAll("\\*\\*", ".*")
                    .replaceAll("\\*", ".*");
        }
}