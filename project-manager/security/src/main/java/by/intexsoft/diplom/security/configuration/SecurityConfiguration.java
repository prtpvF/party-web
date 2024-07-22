package by.intexsoft.diplom.security.configuration;

import by.intexsoft.diplom.security.jwt.JwtUtil;
import by.intexsoft.diplom.security.logout.LogoutHandlerImpl;
import by.intexsoft.diplom.security.security.PersonDetails;
import by.intexsoft.diplom.security.service.PersonDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

        private final LogoutHandlerImpl logoutHandler;
        private final JwtUtil jwtUtil;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests((request) -> request.requestMatchers("/auth/registration",
                    "/auth/login",
                    "/public/party/**",
                    "/public/person/find",
                    "/auth/logout").permitAll().anyRequest().authenticated());
            http.logout(logout ->
                    logout
                            .logoutUrl("/auth/logout")
                            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)))
                    .exceptionHandling(exceptionHandling ->
                            exceptionHandling
                                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                    .csrf((csrf) -> csrf.disable());
                    http.logout((logout) -> logout.logoutUrl("/auth/logout").addLogoutHandler(logoutHandler));
            return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
        }
}