package by.intexsoft.diplom.common_module.repository;

import by.intexsoft.diplom.common_module.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Person findByUsername(String username);
}
