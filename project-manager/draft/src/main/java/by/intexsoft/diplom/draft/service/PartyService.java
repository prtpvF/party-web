package by.intexsoft.diplom.draft.service;

import by.intexsoft.diplom.common.model.party.PartyEntity;
import by.intexsoft.diplom.common.repository.party.PartyRepository;
import by.intexsoft.diplom.draft.exception.PartyNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartyService {

        private final PartyRepository partyRepository;

        public PartyEntity findById(Integer partyId) {
            return partyRepository.findById(partyId)
                    .orElseThrow(() -> new PartyNotFoundException("Cannot find party"));
        }
}