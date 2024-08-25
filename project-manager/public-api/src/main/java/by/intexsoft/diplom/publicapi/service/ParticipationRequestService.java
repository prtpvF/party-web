package by.intexsoft.diplom.publicapi.service;

import by.intexsoft.diplom.common.model.party.PartyEntity;
import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.common.model.request.ParticipationRequestModel;
import by.intexsoft.diplom.common.repository.request.ParticipationRequestRepository;
import by.intexsoft.diplom.publicapi.exception.ParticipationRequestNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipationRequestService {

        private final ParticipationRequestRepository participationRequestRepository;

        public ParticipationRequestModel findByPersonAndParty(PersonModel person,
                                                              PartyEntity party) {
            return participationRequestRepository.findByPersonAndParty(person, party)
                    .orElseThrow(() -> new ParticipationRequestNotFoundException(
                            "cannot find participation request with this id"
                    ));
        }
}
