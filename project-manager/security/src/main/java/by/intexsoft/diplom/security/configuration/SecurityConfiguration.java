package by.intexsoft.diplom.security.configuration;


import by.intexsoft.diplom.security.keycloak.KeycloakLogoutHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(
        securedEnabled = true)
public class SecurityConfiguration {

        private final KeycloakLogoutHandler keycloakLogoutHandler;
        private static final String GROUPS = "groups";
        private static final String REALM_ACCESS_CLAIM = "realm_access";
        private static final String ROLES_CLAIM = "roles";

        @Value("${spring.security.oauth2.client.provider.keycloak.jwk-set-uri}")
        private String SET_URI;

        @PostConstruct
        public void init() {
            System.out.println("JWK Set URI: " + SET_URI);
        }
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests((request) -> request.requestMatchers(
                            "/auth/registration",
                            "/auth/login",
                            "/public/party/**",
                            "/public/person/find",
                            "/auth/logout",
                            "/swagger-ui/**",
                            "/verification/email",
                            "/v3/api-docs/**").permitAll().anyRequest().authenticated());
            http.oauth2ResourceServer((oauth2) -> oauth2
                    .jwt(
                            jwt -> {
                                JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
                                jwtAuthenticationConverter.setPrincipalClaimName("preferred_username");
                                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter);
                            }
                    ));
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
            http.oauth2Login(Customizer.withDefaults()
        )
                    .logout(logout -> logout.addLogoutHandler(keycloakLogoutHandler).logoutSuccessUrl("/"));
            http.csrf(CsrfConfigurer::disable);
            return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public GrantedAuthoritiesMapper userAuthoritiesMapperForKeycloak() {
            return authorities -> {
                Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
                var authority = authorities.iterator().next();
                boolean isOidc = authority instanceof OidcUserAuthority;

                if (isOidc) {
                    var oidcUserAuthority = (OidcUserAuthority) authority;
                    var userInfo = oidcUserAuthority.getUserInfo();

                    if (userInfo.hasClaim(REALM_ACCESS_CLAIM)) {
                        var realmAccess = userInfo.getClaimAsMap(REALM_ACCESS_CLAIM);
                        var roles = (Collection<String>) realmAccess.get(ROLES_CLAIM);
                        mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
                    } else if (userInfo.hasClaim(GROUPS)) {
                        Collection<String> roles = userInfo.getClaim(
                                GROUPS);
                        mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
                    }
                } else {
                    var oauth2UserAuthority = (OAuth2UserAuthority) authority;
                    Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();

                    if (userAttributes.containsKey(REALM_ACCESS_CLAIM)) {
                        Map<String, Object> realmAccess = (Map<String, Object>) userAttributes.get(
                                REALM_ACCESS_CLAIM);
                        Collection<String> roles = (Collection<String>) realmAccess.get(ROLES_CLAIM);
                        mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
                    }
                }
                return mappedAuthorities;
            };
        }

        Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> roles) {
            return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(
                    Collectors.toList());
        }

        @Bean
        public JwtDecoder jwtDecoder() {
            return NimbusJwtDecoder.withJwkSetUri(SET_URI)
                    .build();
        }
}