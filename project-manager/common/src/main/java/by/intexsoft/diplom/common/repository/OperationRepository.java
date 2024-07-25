package by.intexsoft.diplom.common.repository;

import by.intexsoft.diplom.common.model.OperationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository<OperationModel, Integer> {
}
