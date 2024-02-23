package com.auth.authmicroservice.Repository;

import com.auth.authmicroservice.Model.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizerRepository extends JpaRepository<Organizer, Integer> {
}
