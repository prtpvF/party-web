package by.intexsoft.diplom.common.repository;

import by.intexsoft.diplom.common.model.role.PersonRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<PersonRole, Integer> {
    Optional<PersonRole> findByRoleName(String role);
}
