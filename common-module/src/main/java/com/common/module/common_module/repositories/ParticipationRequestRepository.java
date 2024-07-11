package com.common.module.common_module.repositories;

import com.common.module.common_module.models.ParticipationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Integer> {
}
