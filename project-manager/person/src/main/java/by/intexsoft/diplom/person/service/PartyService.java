package by.intexsoft.diplom.person.service;

import by.intexsoft.diplom.common.model.party.PartyEntity;
import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.common.model.role.PartyTypeModel;
import by.intexsoft.diplom.common.repository.party.PartyRepository;
import by.intexsoft.diplom.common.repository.party.PartyTypeRepository;
import by.intexsoft.diplom.person.dto.GuestDto;
import by.intexsoft.diplom.person.dto.ParticipationRequestDto;
import by.intexsoft.diplom.person.exception.PartyNotFoundException;
import by.intexsoft.diplom.person.exception.PartyTypeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartyService {

        private final PartyRepository partyRepository;
        private final PartyTypeRepository partyTypeRepository;

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

        public Page<PartyEntity> findAllOrganizerParties(PersonModel organizer, Pageable pageable) {

            Page<PartyEntity> foundedParties = partyRepository.findAllByOrganizer(organizer, pageable);
            if(foundedParties.isEmpty()) {
                throw new PartyNotFoundException("you don't have any created parties yet");
            }
            return  foundedParties;
        }

        /**
         * method retrieves party model from participation request
         * @param request - ParticipationRequestDto object
         * @return founded party
         */
        public PartyEntity retrievePartyFromRequest(ParticipationRequestDto request){
            return partyRepository.findById(request.getPartyDto().getId()).orElseThrow(
                    () -> new PartyNotFoundException(
                            "cannot find party with this id")
            );
        }

        public PartyTypeModel getPartyType(String typeName){
            return partyTypeRepository.findByType(typeName)
                    .orElseThrow(() -> new PartyTypeNotFoundException("type not found"));
        }

        public int updatePartyRate(Integer partyId, Integer rate) {
            return partyRepository.updatePartyRating(partyId, rate);
        }
}
