package by.intexsoft.diplom.draft.service.draft;

import by.intexsoft.diplom.common.model.draft.PartyDraftAbstract;
import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.common.model.status.PartyDraftStatusModel;
import by.intexsoft.diplom.draft.dto.PartyDraftDto;
import by.intexsoft.diplom.draft.exception.IllegalDraftOwnerException;
import by.intexsoft.diplom.draft.service.StatusService;
import by.intexsoft.diplom.draft.service.TypeService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class which store all CRUD method for any type of draft.
 * Every service which extends this class must override 4 method.
 * Class also store helpfully method for setting fields.
 * @version 1.0
 * @author Mihail Chaplygin
 */
public abstract class AbstractPartyDraftService<T extends PartyDraftAbstract> {

        private final ModelMapper modelMapper;
        private final StatusService statusService;
        private final TypeService typeService;

        protected AbstractPartyDraftService(ModelMapper modelMapper,
                                            StatusService statusService,
                                            TypeService typeService) {
            this.modelMapper = modelMapper;
            this.statusService = statusService;
            this.typeService = typeService;
        }

        public abstract HttpStatus createDraft(PartyDraftDto dto,
                                           Principal principal);


        public  abstract PartyDraftDto getDraftById(Principal principal,
                                          Integer draftId);

        protected List<PartyDraftDto> getAllDrafts(PersonModel personModel) {
            List<T> drafts = new ArrayList<>();
            drafts.addAll((List<T>) personModel.getPartyCreatingDrafts());
            drafts.addAll((List<T>) personModel.getPartyUpdatingDrafts());
            return convertListToDto(drafts);
        }

        public abstract HttpStatus updateDraft(Principal principal,
                                               PartyDraftDto partyDraftDto,
                                               Integer draftNeedToBeUpdatedId);

        public abstract HttpStatus deleteDraft(Principal principal, Integer draftId);

        public T mapAdditionalFields(PartyDraftDto partyDraftDto,
                                      T partyDraftAbstract,
                                      PersonModel personModel) {
            setStatus(partyDraftDto.getStatusId(), partyDraftAbstract);

            partyDraftAbstract.setType(typeService
                    .findById(partyDraftDto
                            .getTypeId()));

            partyDraftAbstract.setOwner(personModel);

            partyDraftAbstract.setStatus(statusService
                    .findById(partyDraftDto
                            .getStatusId()));
            return partyDraftAbstract;
        }

        public void isDraftBelongToPerson(PersonModel personModel, T draft) {
            if(!draft.getOwner().equals(personModel)) {
                throw new IllegalDraftOwnerException("you are not owner of this draft!");
            }
        }

        private void setStatus(Integer statusId, T draftModel) {
            PartyDraftStatusModel statusModel = statusService.findById(statusId);
            draftModel.setStatus(statusModel);
        }

        private List<PartyDraftDto> convertListToDto(List<T> draftList) {
            List<PartyDraftDto> draftDtoList = new ArrayList<>();
            for (T draft : draftList) {
                draftDtoList.add(modelMapper.map(draft, PartyDraftDto.class));
            }
            return draftDtoList;
        }

}
