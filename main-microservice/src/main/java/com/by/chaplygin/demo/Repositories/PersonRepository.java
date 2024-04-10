package com.by.chaplygin.demo.Repositories;

import com.by.chaplygin.demo.Model.Person;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByUsername(String username);
    @EntityGraph(attributePaths = {"allOrgParty"})
    List<Person> findAll();
    Set<Person> findAllByCity(String city);
}
