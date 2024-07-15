package by.intexsoft.diplom.common_module.repository;

import by.intexsoft.diplom.common_module.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByUsername(String username);
    Optional<Person> findByEmail(String email);
    Optional<Person> findByUsernameOrEmail(String username, String email);
    Optional<Person> findByUsernameAndPassword(String username, String email);


}
