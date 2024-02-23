package com.by.chaplygin.demo.Repositories;

import com.by.chaplygin.demo.Model.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartyRepository extends JpaRepository<Party, Integer> {
    List<Party> findByName(String name);
    List<Party> findByAddress (String address);
}
