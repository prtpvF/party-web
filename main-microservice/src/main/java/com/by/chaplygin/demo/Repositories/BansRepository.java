package com.by.chaplygin.demo.Repositories;

import com.by.chaplygin.demo.Model.Bans;
import com.by.chaplygin.demo.Model.Organizer;
import com.by.chaplygin.demo.Model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BansRepository extends JpaRepository<Bans, Integer> {

    Optional<Bans> findBansByPersonAndOrganizer(Person person, Organizer organizer);

}
