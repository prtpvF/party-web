package by.intexsoft.diplom.person.service;

import by.intexsoft.diplom.common.model.party.PartyEntity;
import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.common.repository.party.PartyRepository;
import by.intexsoft.diplom.person.dto.ParticipationRequestDto;
import by.intexsoft.diplom.person.exception.PartyNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartyService {

        private final PartyRepository partyRepository;

        public PartyEntity findPartyById(Integer partyId) {
            return partyRepository.findById(partyId)
                    .orElseThrow(() -> new PartyNotFoundException(
                            "cannot find party!"));
        }

        public void addPersonToPartyGuest(PersonModel person, PartyEntity party) {
            party.getGuests().add(person);
            person.getParties().add(party);
            partyRepository.save(party);
        }

        /**
         * method retrieves party model from participation request
         * @param request - ParticipationRequestDto object
         * @return founded party
         */
        public PartyEntity retrievePartyFromRequest(ParticipationRequestDto request){
            return partyRepository.findById(request.getPartyId()).orElseThrow(
                    () -> new PartyNotFoundException(
                            "cannot find party with this id")
            );
        }

        public List<PartyEntity> findAllOrganizerParties(PersonModel organizer) {

            List<PartyEntity> foundedParties = partyRepository.findAllByOrganizer(organizer);
               if(foundedParties.isEmpty()) {
                   throw new PartyNotFoundException("you don't have any created parties yet");
               }
               return  foundedParties;
        }
}
