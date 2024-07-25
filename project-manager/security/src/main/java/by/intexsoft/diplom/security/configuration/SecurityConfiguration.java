package by.intexsoft.diplom.configuration;

import by.intexsoft.diplom.logout.LogoutHandlerImpl;
import by.intexsoft.diplom.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(
        securedEnabled = true)
public class SecurityConfiguration {

        private final LogoutHandlerImpl logoutHandler;
        private final JwtFilter filter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests((request) -> request.requestMatchers("/auth/registration",
                    "/auth/login",
                    "/public/party/**",
                    "/public/person/find",
                    "/auth/logout").permitAll().anyRequest().authenticated())
                    .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
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


}