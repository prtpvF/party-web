package by.intexsoft.diplom.security.security;

import by.intexsoft.diplom.common.model.PersonModel;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;


@Getter
public class PersonDetails implements UserDetails {

        private final PersonModel personModel;
        private final Set<GrantedAuthority> authorities;

        public PersonDetails(PersonModel personModel, Set<GrantedAuthority> authorities) {
            this.personModel = personModel;
            this.authorities = authorities;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
           return authorities;
        }

        @Override
        public String getPassword() {
            return this.personModel.getPassword();
        }

        @Override
        public String getUsername() {
            return this.personModel.getUsername();
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
