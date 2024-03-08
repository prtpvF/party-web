package com.by.chaplygin.demo.Repositories;

import com.by.chaplygin.demo.Model.Organizer;
import com.by.chaplygin.demo.Model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Repository
public interface OrganizerRepository extends JpaRepository<Organizer, Integer> {
    Optional<Organizer> findByUsername(String username);
}
