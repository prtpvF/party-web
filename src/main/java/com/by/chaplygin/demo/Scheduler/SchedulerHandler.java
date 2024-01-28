package com.by.chaplygin.demo.Scheduler;

import com.by.chaplygin.demo.Model.Party;
import com.by.chaplygin.demo.Repositories.PartyRepository;
import lombok.RequiredArgsConstructor;
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

    @Scheduled(cron = "${event.expiration.date}")
    public void deletePartyWithExpiredDate() {
        List<Party> partys = partyRepository.findAll(); //todo n+1 error
        List<Party> filteredList = partys.stream()
                        .filter(party -> party.getDateOfEvent()
                        .isBefore(LocalDateTime.now())).toList();
        filteredList.forEach(party -> partyRepository.delete(party));

    }
}
