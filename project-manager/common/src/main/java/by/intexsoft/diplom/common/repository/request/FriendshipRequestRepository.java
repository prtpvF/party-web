package by.intexsoft.diplom.common.repository.request;

import by.intexsoft.diplom.common.model.request.FriendshipRequestModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRequestRepository extends JpaRepository<FriendshipRequestModel, Integer> {
}
