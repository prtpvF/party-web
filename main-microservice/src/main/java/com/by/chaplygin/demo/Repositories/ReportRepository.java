package com.by.chaplygin.demo.Repositories;

import com.by.chaplygin.demo.Model.Party;
import com.by.chaplygin.demo.Model.Person;
import com.by.chaplygin.demo.Model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    Optional<Report> findReportByPersonAndParty(Person person, Party party);
    List<Report> findReportsByPerson(Person person);
    List<Report> findReportsByParty(Party party);
}
