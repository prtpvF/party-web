package by.intexsoft.diplom.common_module.repository;

import by.intexsoft.diplom.common_module.model.role.PersonRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<PersonRole, Integer> {
}
