package com.by.chaplygin.demo.Scheduler;

import com.by.chaplygin.demo.Model.Party;
import com.by.chaplygin.demo.Repositories.PartyRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@EnableScheduling
@EnableAsync
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
@RequiredArgsConstructor
@Component
public class SchedulerHandler {
    private final PartyRepository partyRepository;
    private final EntityManager entityManager;

    @Scheduled(cron = "${event.expiration.date}")
    public void deletePartyWithExpiredDate() {
        Session session = entityManager.unwrap(Session.class);
        List<Party> parties = partyRepository.findAll();
        List<Party> filteredList = parties.stream()
                        .filter(party -> party.getDateOfEvent()
                        .isBefore(LocalDateTime.now())).toList();
        filteredList.forEach(party -> partyRepository.delete(party));
    }

}
