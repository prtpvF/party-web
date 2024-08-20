package by.intexsoft.diplom.draft.controller;

import by.intexsoft.diplom.common.model.enums.PartyDraftTypeEnum;
import by.intexsoft.diplom.draft.dto.PartyDraftDto;
import by.intexsoft.diplom.draft.service.draft.CreatingDraftService;
import by.intexsoft.diplom.draft.service.draft.UpdatingDraftService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/draft")
@RequiredArgsConstructor
public class PartyDraftController {

        private final CreatingDraftService creatingDraftService;
        private final UpdatingDraftService updatingDraftService;

        @Operation(summary = "Gets a party draft by ID",
                description = "Returns a party draft by its ID. Requires a 'type' parameter in the request, " +
                        "which should be 'CREATING' or 'UPDATING' in uppercase. The method selects the appropriate " +
                        "draft type based on this parameter. If the draft doesn't belong to the authenticated user or " +
                        "does not exist, a 403 status code with an error message is returned.")
        @GetMapping("/{draftId}")
        public PartyDraftDto getPartyDraft(Principal principal,
                                           @PathVariable("draftId") Integer draftId,
                                           @RequestParam("type") String draftType) {
            if(draftType.equals(PartyDraftTypeEnum.CREATING.name())) {
                    return creatingDraftService.getDraftById(principal, draftId);
            }
            else {
                    return updatingDraftService.getDraftById(principal, draftId);
            }
        }

        @Operation(summary = "Get all party drafts for the authenticated user",
                description = "Retrieves all party drafts associated with the authenticated user. If the drafts " +
                        "do not belong to the user, a 403 status code with an error message is returned.")
        @GetMapping("/all")
        public List<PartyDraftDto> getAllPersonDrafts(Principal principal) {
            return creatingDraftService.getAllDtoDrafts(principal);
        }

        @Operation(summary = "Create a new party draft",
                description = "Creates a new party draft that will be used for either creating or updating a party. " +
                        "The 'type' parameter must be included in the request as either 'CREATING' or 'UPDATING' " +
                        "in uppercase to specify the draft type. If 'UPDATING' is selected, the request body " +
                        "must include the party ID.")
        @PostMapping("/new/creation")
        public HttpStatus addDraft(@RequestBody PartyDraftDto partyDraftDto,
                                   @RequestParam("type") String draftType,
                                   Principal principal) {
               if(draftType.equals(PartyDraftTypeEnum.CREATING.name())) {
                       return creatingDraftService.createDraft(partyDraftDto, principal);
               }
               else {
                       return updatingDraftService.createDraft(partyDraftDto, principal);
               }
        }

        @Operation(summary = "Delete a party draft",
                description = "Deletes a party draft by its ID. Requires a 'type' parameter in the request, " +
                        "which should be 'CREATING' or 'UPDATING' in uppercase. The method selects the appropriate " +
                        "draft type based on this parameter. If the draft doesn't belong to the authenticated user, " +
                        "a 403 status code with an error message is returned.")
        @DeleteMapping("/{draftId}")
        public HttpStatus deleteDraft(@PathVariable("draftId") Integer draftId,
                                      Principal principal,
                                      @RequestParam("type") String draftType) {
           if(draftType.equals(PartyDraftTypeEnum.CREATING.name())) {
                   return creatingDraftService.deleteDraft(principal, draftId);
           }
           else {
                   return updatingDraftService.deleteDraft(principal, draftId);
           }
        }

        @Operation(summary = "Update a party draft",
                description = "Updates an existing party draft by its ID. Requires a 'type' parameter in the request, " +
                        "which should be 'CREATING' or 'UPDATING' in uppercase. The method selects the appropriate " +
                        "draft type based on this parameter. If the draft doesn't belong to the authenticated user, " +
                        "a 403 status code with an error message is returned.")
        @PatchMapping("/{draftId}")
        public HttpStatus updateDraft(@PathVariable("draftId") Integer draftId,
                                      @RequestBody PartyDraftDto partyDraftDto,
                                      @RequestParam("type") String draftType,
                                      Principal principal) {
            if (draftType.equals(PartyDraftTypeEnum.CREATING.name())) {
                    return creatingDraftService.updateDraft(principal, partyDraftDto, draftId);
            }
            else {
                    return updatingDraftService.updateDraft(principal, partyDraftDto, draftId);
            }
        }
}