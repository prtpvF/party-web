package by.intexsoft.diplom.common_module.repository;

import by.intexsoft.diplom.common_module.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Integer> {
}
