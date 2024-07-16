package by.intexsoft.diplom.common_module.repository;

import by.intexsoft.diplom.common_module.model.role.PersonRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<PersonRole, Integer> {
    Optional<PersonRole> findByRole(String role);
}
