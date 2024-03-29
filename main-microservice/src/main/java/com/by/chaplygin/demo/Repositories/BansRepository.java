package com.by.chaplygin.demo.Repositories;

import com.by.chaplygin.demo.Model.Bans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BansRepository extends JpaRepository<Bans, Integer> {

}
