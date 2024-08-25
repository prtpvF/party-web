package by.intexsoft.diplom.admin.service;

import by.intexsoft.diplom.admin.exception.IllegalPartyStatusException;
import by.intexsoft.diplom.admin.exception.StatusNotFoundException;
import by.intexsoft.diplom.common.model.party.PartyEntity;
import by.intexsoft.diplom.common.model.status.PartyStatusModel;
import by.intexsoft.diplom.common.repository.party.PartyStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartyStatusService {

        private final PartyStatusRepository partyStatusRepository;

        public PartyStatusModel findPartyStatusByName(String statusName) {
            return partyStatusRepository.findByStatus(statusName)
                    .orElseThrow(() -> new StatusNotFoundException("cannot find status with this name"));
        }

        public void isPartyHasCorrectStatus(PartyEntity party, String statusName) {
            if (!party.getStatus().getStatus().equals(statusName)) {
                throw new IllegalPartyStatusException("your status is incorrect");
            }
        }

        public void isStatusExists(Integer statusId) {
            partyStatusRepository.findById(statusId)
                    .orElseThrow(() -> new StatusNotFoundException("cannot find status with this id"));
        }
}

