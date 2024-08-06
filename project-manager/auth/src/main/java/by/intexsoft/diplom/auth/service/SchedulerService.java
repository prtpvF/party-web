package by.intexsoft.diplom.auth.service;

import by.intexsoft.diplom.common.model.PersonModel;
import by.intexsoft.diplom.common.model.enums.PersonStatusEnum;
import by.intexsoft.diplom.common.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Service was created for start thread for deleting person which haven't finished
 * registration process.
 * Method deletes unregistered users every 10 minutes
 * @version 1.0
 * @author Mihail Chaplygin
 */
@Service
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {

        private final PersonRepository personRepository;

        @Scheduled(initialDelay = 5000, fixedDelay = 60000)
        public void scheduleUserDeletion() {
             CompletableFuture.runAsync(() -> {
                    List<PersonModel> persons = findAllUnavailablePersons();
                    List<String> unavailablePersonsUsernames = persons.stream()
                            .filter(user -> user.getStatus().equals(PersonStatusEnum.UNAVAILABLE.name()))
                            .map(PersonModel::getUsername)
                            .toList();
                    if (!unavailablePersonsUsernames.isEmpty()) {
                        personRepository.deleteBatchByUsernames(unavailablePersonsUsernames);
                    }
                  log.info("start cleaning unavailable persons");

            });
        }

        private List<PersonModel> findAllUnavailablePersons() {
            return personRepository.findAllByStatus(PersonStatusEnum.UNAVAILABLE.name());
        }
}
