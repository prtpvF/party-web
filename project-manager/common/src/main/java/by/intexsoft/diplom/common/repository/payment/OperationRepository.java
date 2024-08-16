package by.intexsoft.diplom.common.repository.payment;

import by.intexsoft.diplom.common.model.payment.PartyPaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository<PartyPaymentModel, Integer> {
}
