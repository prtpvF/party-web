package com.by.chaplygin.demo.Repositories;

import com.by.chaplygin.demo.Model.ParticipationRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<ParticipationRequests, Integer> {
}
