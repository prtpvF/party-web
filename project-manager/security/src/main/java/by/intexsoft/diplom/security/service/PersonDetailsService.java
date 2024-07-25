package by.intexsoft.diplom.service;



import by.intexsoft.diplom.common.model.Person;
import by.intexsoft.diplom.common.repository.PersonRepository;
import by.intexsoft.diplom.security.PersonDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PersonDetailsService implements UserDetailsService {

        private final PersonRepository personRepository;

        @Override
        public PersonDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Person person = personRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
            Set<GrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + person.getRole().getRoleName()));
            return new PersonDetails(person, authorities);
        }
}