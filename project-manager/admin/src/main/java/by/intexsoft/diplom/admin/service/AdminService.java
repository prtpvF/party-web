package by.intexsoft.diplom.admin.service;

import by.intexsoft.diplom.admin.dto.PartyDto;
import by.intexsoft.diplom.admin.exception.*;
import by.intexsoft.diplom.admin.kafka.KafkaMessageModel;
import by.intexsoft.diplom.admin.util.ObjectMapper;
import by.intexsoft.diplom.common.model.enums.PersonRolesEnum;
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
import org.springframework.security.access.AccessDeniedException;
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

        private final PartyService partyService;
        private final PersonService personService;
        private final PartyStatusService partyStatusService;

        public List<PartyDto> findAllPartyCrudRequestInAdminCity(Principal principal) {
                personService.checkPersonRole(principal);
                List<PartyEntity> foundedParties = partyService.findAllUnavailableByCity(principal);
                if (foundedParties.isEmpty()) {
                        throw new EmptyPartiesListException("not parties' requests in your city");
                }
                return partyService.convertPartyListToDtoList(foundedParties);
        }

        public Page<PartyDto> findAllPartyRequestsByStatusAndCity(Principal principal,
                                                                  Integer statusId,
                                                                  Pageable pageable) {
                personService.checkPersonRole(principal);
                String city = personService.getCityByPrincipal(principal);
                partyStatusService.isStatusExists(statusId);
                return partyService.findAllByStatusAndCity(statusId, city, pageable);
        }

        public PartyDto getParty(Integer id) {
                PartyEntity party = partyService.findPartyById(id);
                return partyService.convertPartyToDto(party);
        }

        public void answerPartyRequest(Integer partyId,
                                       boolean flag,
                                       Principal principal) {
                personService.checkPersonRole(principal);
                PartyEntity party = partyService.findPartyById(partyId);
                partyService.handlePartyRequest(party, flag, principal);
        }
}