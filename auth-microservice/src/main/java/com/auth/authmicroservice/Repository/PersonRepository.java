package com.auth.authmicroservice.Repository;

import com.auth.authmicroservice.Model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByUsername(String s);
}
