package by.intexsoft.diplom.common.repository;

import by.intexsoft.diplom.common.model.PersonModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PersonRepository extends JpaRepository<PersonModel, Integer> {

        @EntityGraph(attributePaths = {"role", "participationRequests"})
        @Query("SELECT p FROM PersonModel p WHERE p.username = :username")
        Optional<PersonModel> findByUsername(@Param("username") String username);
        Optional<PersonModel> findByEmail(String email);
        Optional<PersonModel> findByUsernameOrEmail(String username, String email);



}
