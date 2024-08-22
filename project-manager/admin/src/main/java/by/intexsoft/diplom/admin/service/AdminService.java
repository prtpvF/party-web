package by.intexsoft.diplom.admin.service;

import by.intexsoft.diplom.admin.dto.PartyDto;
import by.intexsoft.diplom.admin.exception.*;
import by.intexsoft.diplom.admin.kafka.KafkaMessageModel;
import by.intexsoft.diplom.admin.util.ObjectMapper;
import by.intexsoft.diplom.common.model.party.PartyEntity;
import by.intexsoft.diplom.common.model.status.PartyStatusModel;
import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.common.model.enums.PartyStatusEnum;
import by.intexsoft.diplom.common.repository.party.PartyRepository;
import by.intexsoft.diplom.common.repository.party.PartyStatusRepository;
import by.intexsoft.diplom.common.repository.person.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

/**
 * <h3><b>service with admin functional.
 * For image updating look in person-module's controllers.
 * <br>1) save new images(if necessary)
 * <br>2) set new images to party and save
 * <br>3) delete old images(if necessary)</h3>
 * </b>
 * <b><p>@author Mihail Chaplygin</p></b>
 * <b>@version 1.0</b>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {

        private final PartyRepository partyRepository;
        private final PersonRepository personRepository;
        private final PartyStatusRepository partyStatusRepository;
        private final ObjectMapper objectMapper;
        private final KafkaTemplate<String, KafkaMessageModel> kafkaTemplate;

        @Value("${spring.kafka.topic-organizer.name}")
        private String organizerTopic;

        @Value("${request-apply}")
        private String applyRequestText;

        @Value("${request-decline}")
        private String declineRequestText;

        public List<PartyDto> findAllPartyCrudRequestInAdminCity(Principal principal) {
                List<PartyEntity> foundedParties = partyRepository.findAllUnavailableByCity(
                        principal.getName());
                if (foundedParties.isEmpty()) {
                        throw new EmptyPartiesListException("not parties' requests in your city");
                }
                return objectMapper.convertPartyListToDtoList(foundedParties);
        }

        public Page<PartyDto> findAllPartyRequestsByStatusAndCity(Principal principal,
                                                                  Integer statusId,
                                                                  Pageable pageable) {
                String city = getCityByPrincipal(principal);
                isStatusExists(statusId);
                Page<PartyEntity> page = partyRepository.findAllByStatusAndCity(statusId, city, pageable);
                return page.map(party -> objectMapper.convertPartyToDto(party));
        }

        public PartyDto getParty(Integer id) {
                PartyEntity party = findPartyById(id);
                return objectMapper.convertPartyToDto(party);
        }

        /**
         * method manipulates party crud methods. The result depends on party status which is extracted from party and flag
         * @param partyId id of party model which will be an object of manipulations
         * @param flag admin's decision. If flag is true then the method required by the organizer will be executed
         */
        public void answerPartyRequest(Integer partyId, boolean flag) {
                PartyEntity party = findPartyById(partyId);
                String statusName = party.getStatus().getStatus().toUpperCase();

                switch (PartyStatusEnum.valueOf(statusName)) {
                        case WAIT_FOR_DELETING:
                                deleteParty(party);
                                break;
                        case WAIT_FOR_UPDATING:
                                updateParty(party, flag);
                                break;
                        case WAIT_FOR_CREATING:
                                createParty(party, flag);
                                break;
                        default:
                                break;
                }
        }

        private void deleteParty(PartyEntity party) {
                isPartyHasCorrectStatus(party, PartyStatusEnum.WAIT_FOR_DELETING.name());
                        //todo return all payments to user
                partyRepository.delete(party);
                sendMessageToOrganizerTopic(party, true);
        }

        private void processParty(PartyEntity party,
                                  Boolean isAccept,
                                  PartyStatusEnum expectedStatus,
                                  String successLogMessage) {

                isPartyHasCorrectStatus(party, expectedStatus.name());

                if (Boolean.TRUE.equals(isAccept)) {
                        party.setStatus(findPartyStatusByName(PartyStatusEnum.AVAILABLE.name()));
                        partyRepository.save(party);
                        log.info(successLogMessage + " party id:{}", party.getId());
                }
                else {
                        log.info(expectedStatus.name() + " was rejected. party id:{}", party.getId());

                        party.setStatus(findPartyStatusByName(
                                PartyStatusEnum.PENDING_REVISION.name()));
                        partyRepository.save(party);
                        sendMessageToOrganizerTopic(party, isAccept);
                        throw new IllegalPartyDataException(
                                "Provided data is incorrect. You must to change it!");
                }
        }

        private void updateParty(PartyEntity party, Boolean isAccept) {
                processParty(party, isAccept, PartyStatusEnum.WAIT_FOR_UPDATING,
                        "party updating was saved.");
        }

        private void createParty(PartyEntity party, Boolean isAccept) {
                processParty(party, isAccept, PartyStatusEnum.WAIT_FOR_CREATING,
                        "party creating was saved.");
        }

        private PartyEntity findPartyById(Integer partyId) {
               return partyRepository.findById(partyId)
                        .orElseThrow(() -> new PartyNotFoundException("cannot find the party with this id"));
        }

        private void isPartyHasCorrectStatus(PartyEntity party, String statusName) {
                if(!party.getStatus().getStatus().equals(statusName)) {
                        throw new IllegalPartyStatusException("your status is incorrect");
                }
        }

        private PartyStatusModel findPartyStatusByName(String statusName) {
               return partyStatusRepository.findByStatus(statusName)
                       .orElseThrow(() -> new StatusNotFoundException("cannot find status with this name"));
        }

        private PersonModel findPersonByUsername(String username) {
                return personRepository.findByUsername(username)
                        .orElseThrow(() -> new PersonNotFoundException(
                                "cannot find person with username " + username));
        }

        private String getCityByPrincipal(Principal principal) {
                PersonModel person = findPersonByUsername(principal.getName());
                return person.getCity();
        }

        private void isStatusExists(Integer statusId) {
                partyStatusRepository.findById(statusId)
                        .orElseThrow(() -> new StatusNotFoundException(
                                "cannot find status with this id"));
        }

        private void sendMessageToOrganizerTopic(PartyEntity party, Boolean flag) {
                KafkaMessageModel kafkaMessage = new KafkaMessageModel();
                PersonModel organizer = party.getOrganizer();
                kafkaMessage.setTopic(organizerTopic);
                kafkaMessage.setUsername(organizer.getUsername());
                kafkaMessage.setToEmail(organizer.getEmail());

                if(flag) {
                        kafkaMessage.setData(String.format(applyRequestText,
                                organizer.getUsername(),
                                party.getStatus().getStatus(),
                                party.getName()));
                }
                else {
                        kafkaMessage.setData(String.format(declineRequestText,
                                organizer.getUsername(),
                                party.getStatus().getStatus(),
                                party.getName()));
                }
                kafkaTemplate.send(organizerTopic, kafkaMessage);

        }
}