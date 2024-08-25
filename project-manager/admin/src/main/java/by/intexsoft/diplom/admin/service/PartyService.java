package by.intexsoft.diplom.admin.service;

import by.intexsoft.diplom.admin.dto.PartyDto;
import by.intexsoft.diplom.admin.exception.IllegalPartyDataException;
import by.intexsoft.diplom.admin.exception.IllegalPartyOwnerException;
import by.intexsoft.diplom.admin.exception.PartyNotFoundException;
import by.intexsoft.diplom.admin.exception.StatusNotFoundException;
import by.intexsoft.diplom.admin.util.ObjectMapper;
import by.intexsoft.diplom.common.model.enums.PartyStatusEnum;
import by.intexsoft.diplom.common.model.party.PartyEntity;
import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.common.repository.party.PartyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PartyService {

        private final PartyRepository partyRepository;
        private final PersonService personService;
        private final PartyStatusService partyStatusService;
        private final KafkaMessageService kafkaMessageService;
        private final ObjectMapper objectMapper;

        public List<PartyEntity> findAllUnavailableByCity(Principal principal) {
            String city = getCityByPrincipal(principal);
            return partyRepository.findAllUnavailableByCity(city);
        }

        public Page<PartyDto> findAllByStatusAndCity(Integer statusId, String city, Pageable pageable) {
            Page<PartyEntity> page = partyRepository.findAllByStatusAndCity(statusId, city, pageable);
            return page.map(objectMapper::convertPartyToDto);
        }

        public PartyEntity findPartyById(Integer partyId) {
            return partyRepository.findById(partyId)
                    .orElseThrow(() -> new PartyNotFoundException("Cannot find the party with this id"));
        }

        public void handlePartyRequest(PartyEntity party, Boolean flag, Principal principal) {
            String statusName = party.getStatus().getStatus().toUpperCase();

            switch (PartyStatusEnum.valueOf(statusName)) {
                case WAIT_FOR_DELETING:
                    deleteParty(principal, party);
                    break;
                case WAIT_FOR_UPDATING:
                    updateParty(party, flag, principal);
                    break;
                case WAIT_FOR_CREATING:
                    createParty(party, flag, principal);
                    break;
                default:
                    break;
            }
        }

        public List<PartyDto> convertPartyListToDtoList(List<PartyEntity> parties) {
            return objectMapper.convertPartyListToDtoList(parties);
        }

        public PartyDto convertPartyToDto(PartyEntity party) {
            return objectMapper.convertPartyToDto(party);
        }

        private void deleteParty(Principal principal, PartyEntity party) {
            isPartyBelongsToOrganizer(principal, party);
            partyRepository.delete(party);
            kafkaMessageService.sendMessageToOrganizerTopic(party, true);
            // todo return all payments to user
        }

        private void updateParty(PartyEntity party, Boolean isAccept, Principal principal) {
            isPartyBelongsToOrganizer(principal, party);
            processParty(party, isAccept, PartyStatusEnum.WAIT_FOR_UPDATING, "Party updating was saved.");
        }

        private void createParty(PartyEntity party, Boolean isAccept, Principal principal) {
            isPartyBelongsToOrganizer(principal, party);
            processParty(party, isAccept, PartyStatusEnum.WAIT_FOR_CREATING, "Party creating was saved.");
        }

        private void processParty(PartyEntity party,
                                  Boolean isAccept,
                                  PartyStatusEnum expectedStatus,
                                  String successLogMessage) {
            partyStatusService.isPartyHasCorrectStatus(party, expectedStatus.name());

            if (Boolean.TRUE.equals(isAccept)) {
                party.setStatus(partyStatusService.findPartyStatusByName(PartyStatusEnum.AVAILABLE.name()));
                partyRepository.save(party);
                log.info(successLogMessage + " party id:{}", party.getId());
            } else {
                log.info(expectedStatus.name() + " was rejected. party id:{}", party.getId());
                party.setStatus(partyStatusService.findPartyStatusByName(PartyStatusEnum.PENDING_REVISION.name()));
                partyRepository.save(party);
                kafkaMessageService.sendMessageToOrganizerTopic(party, isAccept);
                throw new IllegalPartyDataException("Provided data is incorrect. You must change it!");
            }
        }

        private String getCityByPrincipal(Principal principal) {
            PersonModel person = personService.findPersonByUsername(principal.getName());
            return person.getCity();
        }

        private void isPartyBelongsToOrganizer(Principal principal, PartyEntity party) {
        PersonModel organizer = personService.findPersonByUsername(principal.getName());
        if (!party.getOrganizer().equals(organizer)) {
            throw new IllegalPartyOwnerException("You are not the owner of this party");
        }
    }
}