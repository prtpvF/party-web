package by.intexsoft.diplom.common.repository.status;

import by.intexsoft.diplom.common.model.status.ParticipationRequestStatusModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipationRequestStatusRepository extends JpaRepository<
                                                ParticipationRequestStatusModel, Integer> {

        Optional<ParticipationRequestStatusModel> findByStatusName(String statusName);
}
