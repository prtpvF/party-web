package by.intexsoft.diplom.draft.service;

import by.intexsoft.diplom.common.model.enums.PartyDraftStatusEnum;
import by.intexsoft.diplom.common.model.party.PartyDraftModel;
import by.intexsoft.diplom.common.model.party.PartyEntity;
import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.common.repository.party.PartyDraftRepository;
import by.intexsoft.diplom.draft.dto.PartyDraftDto;
import by.intexsoft.diplom.draft.exception.DraftNotFoundException;
import by.intexsoft.diplom.draft.util.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

import static by.intexsoft.diplom.common.model.enums.PartyDraftTypeEnum.UPDATING;
import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartyDraftService {

        private final PartyDraftRepository partyDraftRepository;
        private final PersonService personService;
        private final PartyService partyService;
        private final StatusService statusService;
        private final TypeService typeService;
        private final ObjectMapper objectMapper;

        public PartyDraftDto getDraftById(Principal principal, Integer draftId) {
                PartyDraftModel draftModel = findDraftById(draftId);
                PersonModel personModel = personService.findByPrincipal(principal);
                personService.verifyDraftOwnership(personModel, draftModel);
                return objectMapper.convertToDto(draftModel);
        }

        public List<PartyDraftDto> getAllDtoDrafts(Principal principal) {
                PersonModel personModel = personService.findByPrincipal(principal);
                return objectMapper.convertListToDto(personModel.getPartyDrafts());
        }

        public HttpStatus createDraft(Principal principal, PartyDraftDto partyDraftDto) {
                PartyDraftModel draftModel = objectMapper.convertFromDto(partyDraftDto);
                typeService.assignDraftType(partyDraftDto, draftModel);
                initializeDraft(principal, partyDraftDto, draftModel);
                partyDraftRepository.save(draftModel);
                return OK;
        }

        @Transactional
        public HttpStatus deletePartyDraftModel(Principal principal, Integer id) {
                PartyDraftModel draftModel = findDraftById(id);
                PersonModel personModel = personService.findByPrincipal(principal);
                personService.verifyDraftOwnership(personModel, draftModel);
                partyDraftRepository.delete(draftModel);
                return OK;
        }

        public HttpStatus updatePartyDraftModel(Principal principal,
                                                Integer draftId,
                                                PartyDraftDto updatedDraftDto) {
                PartyDraftModel draftNeedToBeUpdated = findDraftById(draftId);
                PersonModel personModel = personService.findByPrincipal(principal);
                personService.verifyDraftOwnership(personModel, draftNeedToBeUpdated);
                draftNeedToBeUpdated = objectMapper.updatePartyDraft(updatedDraftDto, draftNeedToBeUpdated);
                draftNeedToBeUpdated.setStatus(statusService.findByName(PartyDraftStatusEnum.UPDATED.name()));
                draftNeedToBeUpdated.setParty(partyService.findById(updatedDraftDto.getPartyId()));
                partyDraftRepository.save(draftNeedToBeUpdated);
                return OK;
        }

        private void initializeDraft(Principal principal,
                                     PartyDraftDto partyDraftDto,
                                     PartyDraftModel draftModel) {
                draftModel.setStatus(statusService.findById(partyDraftDto.getStatusId()));
                PersonModel personModel = personService.findByPrincipal(principal);
                draftModel.setOwner(personModel);
                personService.verifyDraftOwnership(personModel, draftModel);
                if (UPDATING.name().equals(draftModel.getType().getName())) {
                        PartyEntity party = partyService.findById(partyDraftDto.getPartyId());
                        personService.verifyPartyOwnership(party, principal);
                        draftModel.setParty(party);
                }
        }

        private PartyDraftModel findDraftById(Integer id) {
                return partyDraftRepository.findById(id)
                        .orElseThrow(() -> new DraftNotFoundException(
                                "cannot find draft"));
        }
}