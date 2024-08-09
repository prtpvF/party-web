package by.intexsoft.diplom.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * This module was created for auth process like login, reg etc.
 * In the version 1.0 all logic were custom. In this version keycloak was added SSO (keycloak)
 * @author Mihail Chaplygin
 * @version 2.0
 */
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
@ComponentScan(basePackages = {"by.intexsoft.diplom", "by.intexsoft.diplom.security"})
@EnableAsync
public class AuthApplication {

        public static void main(String[] args) {
            SpringApplication.run(AuthApplication.class, args);
        }
}