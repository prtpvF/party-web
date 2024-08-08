package by.intexsoft.diplom.security.service;


import by.intexsoft.diplom.common.model.PersonModel;
import by.intexsoft.diplom.common.repository.PersonRepository;
import by.intexsoft.diplom.security.security.PersonDetails;
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
            PersonModel personModel = personRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
            Set<GrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + personModel.getRole().getRoleName()));
            return new PersonDetails(personModel, authorities);
        }
}