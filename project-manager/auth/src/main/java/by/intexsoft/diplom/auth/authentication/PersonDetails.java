package by.intexsoft.diplom.auth.authentication;

import by.intexsoft.diplom.auth.dto.LoginDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class PersonDetails implements Authentication {

        private final LoginDto loginDto;
        private boolean authenticated;

        public PersonDetails(LoginDto loginDto,
                             boolean authenticated) {

            this.loginDto = loginDto;
            this.authenticated = authenticated;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
        }

        @Override
        public Object getCredentials() {
            return null;
        }

        @Override
        public Object getDetails() {
            return null;
        }

        @Override
        public Object getPrincipal() {
            return loginDto.getUsername();
        }

        @Override
        public boolean isAuthenticated() {
            return authenticated;
        }

        @Override
        public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
            this.authenticated = isAuthenticated;
        }

        @Override
        public String getName() {
            return loginDto.getUsername();
        }
}
