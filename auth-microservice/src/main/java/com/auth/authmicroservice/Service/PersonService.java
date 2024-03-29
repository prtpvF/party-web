package com.auth.authmicroservice.Service;

import com.auth.authmicroservice.Model.Person;
import com.auth.authmicroservice.Repository.PersonRepository;
import com.auth.authmicroservice.Security.PersonDetails;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor

public class PersonService implements UserDetailsService {
    private final PersonRepository personRepository;

    @Override
    @CircuitBreaker(name = "authService")
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByUsername(s);

        if (person.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new PersonDetails(person.get());
    }
}
