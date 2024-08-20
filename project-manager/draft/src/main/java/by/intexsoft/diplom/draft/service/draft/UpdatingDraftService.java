package by.intexsoft.diplom.draft.service.draft;

import by.intexsoft.diplom.common.model.draft.PartyCreateDraftModel;
import by.intexsoft.diplom.common.model.draft.PartyUpdateDraftModel;
import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.common.repository.draft.PartyUpdateDraftRepository;
import by.intexsoft.diplom.draft.dto.PartyDraftDto;
import by.intexsoft.diplom.draft.exception.DraftAlreadyExistsException;
import by.intexsoft.diplom.draft.exception.DraftNotFoundException;
import by.intexsoft.diplom.draft.service.PartyService;
import by.intexsoft.diplom.draft.service.PersonService;
import by.intexsoft.diplom.draft.service.StatusService;
import by.intexsoft.diplom.draft.service.TypeService;
import by.intexsoft.diplom.draft.util.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * Implementation fot updating drafts
 * @version 1.0
 * @author Mihail Chaplygin
 */
@Service
public class UpdatingDraftService extends AbstractPartyDraftService<PartyUpdateDraftModel>{

        private final PartyUpdateDraftRepository partyUpdateDraftRepository;
        private final PersonService personService;
        private final PartyService partyService;
        private final ModelMapper modelMapper;
        private final StatusService statusService;
        private final TypeService typeService;
        private final ObjectMapper objectMapper;

        protected UpdatingDraftService(PartyUpdateDraftRepository partyUpdateDraftRepository,
                                       PersonService personService,
                                       PartyService partyService,
                                       ModelMapper modelMapper,
                                       StatusService statusService,
                                       TypeService typeService,
                                       ObjectMapper objectMapper) {
            super(modelMapper,
                    statusService,
                    typeService);
            this.partyUpdateDraftRepository = partyUpdateDraftRepository;
            this.personService = personService;
            this.partyService = partyService;
            this.modelMapper = modelMapper;
            this.statusService = statusService;
            this.typeService = typeService;
            this.objectMapper = objectMapper;
        }

        @Override
        public HttpStatus createDraft(PartyDraftDto dto, Principal principal) {
            PersonModel personModel = personService.findByPrincipal(principal);
            PartyUpdateDraftModel draft = modelMapper.map(dto, PartyUpdateDraftModel.class);
            super.mapAdditionalFields(dto, draft, personModel);
            draft.setParty(partyService.findById(dto.getPartyId()));
            isDraftAlreadyExist(dto.getPartyId());
            partyUpdateDraftRepository.save(draft);
            return CREATED;
        }

        @Override
        public PartyDraftDto getDraftById(Principal principal, Integer draftId) {
            PersonModel person = personService.findByPrincipal(principal);
            PartyUpdateDraftModel draft = findDraftById(draftId);
            super.isDraftBelongToPerson(person, draft);
            return objectMapper.convertUpdatingDraftToDto(draft);
        }

        @Override
        public HttpStatus updateDraft(Principal principal,
                                      PartyDraftDto partyDraftDto,
                                      Integer draftNeedToBeUpdatedId) {
            PersonModel person = personService.findByPrincipal(principal);
            PartyUpdateDraftModel draftNeedToBeUpdate = findDraftById(draftNeedToBeUpdatedId);
            modelMapper.map(partyDraftDto, draftNeedToBeUpdate);
            draftNeedToBeUpdate.setId(draftNeedToBeUpdate.getId());
            draftNeedToBeUpdate.setOwner(draftNeedToBeUpdate.getOwner());
            draftNeedToBeUpdate.setParty(draftNeedToBeUpdate.getParty());
            super.isDraftBelongToPerson(person, draftNeedToBeUpdate);
            partyUpdateDraftRepository.save(draftNeedToBeUpdate);
            return OK;
        }

        @Override
        public HttpStatus deleteDraft(Principal principal, Integer draftId) {
            PartyUpdateDraftModel draft = findDraftById(draftId);
            PersonModel person = personService.findByPrincipal(principal);
            super.isDraftBelongToPerson(person, draft);
            partyUpdateDraftRepository.delete(draft);
            return OK;
        }

        private PartyUpdateDraftModel findDraftById(Integer draftId) {
            return partyUpdateDraftRepository.findById(draftId)
                    .orElseThrow(() -> new DraftNotFoundException("cannot find draft with this id"));
        }

        private void isDraftAlreadyExist(Integer partyId) {
             partyUpdateDraftRepository.findByPartyId(partyId)
                    .ifPresent( s -> {
                        throw new DraftAlreadyExistsException(
                            "you cant create a new updating draft " +
                                    "because you already have 1");
                    });
        }
}