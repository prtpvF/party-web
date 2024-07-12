package by.intexsoft.diplom.common_module.repository;

import by.intexsoft.diplom.common_module.models.ParticipationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Integer> {
}
