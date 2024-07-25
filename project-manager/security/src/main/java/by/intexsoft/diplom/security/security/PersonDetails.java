package by.intexsoft.diplom.security;

import by.intexsoft.diplom.common.model.Person;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;


@Getter
public class PersonDetails implements UserDetails {

        private final Person person;
        private final Set<GrantedAuthority> authorities;

        public PersonDetails(Person person, Set<GrantedAuthority> authorities) {
            this.person = person;
            this.authorities = authorities;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
           return authorities;
        }

        @Override
        public String getPassword() {
            return this.person.getPassword();
        }

        @Override
        public String getUsername() {
            return this.person.getUsername();
        }
        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
}
