package by.intexsoft.diplom.draft.controller;

import by.intexsoft.diplom.draft.dto.PartyDraftDto;
import by.intexsoft.diplom.draft.service.PartyDraftService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/draft")
@RequiredArgsConstructor
public class PartyDraftController {

        private final PartyDraftService partyDraftService;

        @Value("${api.getPartyDraft.summary}")
        private String getPartyDraftSummary;

        @Value("${api.getPartyDraft.description}")
        private String getPartyDraftDescription;

        @Value("${api.getAllPartyDrafts.summary}")
        private String getAllPartyDraftsSummary;

        @Value("${api.getAllPartyDrafts.description}")
        private String getAllPartyDraftsDescription;

        @Value("${api.addCreatingDraft.summary}")
        private String addCreatingDraftSummary;

        @Value("${api.addCreatingDraft.description}")
        private String addCreatingDraftDescription;

        @Value("${api.deleteDraft.summary}")
        private String deleteDraftDraftSummary;

        @Value("${api.deleteDraft.description}")
        private String deleteDraftDescription;

        @Value("${api.updateDraft.summary}")
        private String updateDraftDraftSummary;

        @Value("${api.updateDraft.description}")
        private String updateDraftDescription;

        @Operation(summary = "Gets party draft by ID",
            description = "returns an error message and 403 status if draft doesn't exist " +
                    "or draft doesn't belong to authenticated user")
        @GetMapping("/{draftId}")
        public PartyDraftDto getPartyDraft(Principal principal,
                                           @PathVariable("draftId") Integer draftId) {
            return partyDraftService.getDraftById(principal, draftId);
        }

        @Operation(summary = "Get all person's party drafts",
            description = "returns an error message and 403 status " +
                    "if drafts don't belong to user")
        @GetMapping("/all")
        public List<PartyDraftDto> getAllPartyDrafts(Principal principal) {
            return partyDraftService.getAllDtoDrafts(principal);
        }

        @Operation(summary = "Create a new party's draft",
                description = "this endpoint creates a draft model which will " +
                        "be used for party creating or updating." +
                        "If it will be used for updating then request body must to include party id")
        @PostMapping("/new/creation")
        public HttpStatus addCreatingDraft(@RequestBody PartyDraftDto partyDraftDto,
                                           Principal principal) {
                return partyDraftService.createDraft(principal, partyDraftDto);
        }

        @Operation(summary = "Delete a party's draft",
            description = "if draft doesn't belong to authenticated user then method returns " +
                    "an error message and 403 status")
        @DeleteMapping("/{draftId}")
        public HttpStatus deleteDraft(@PathVariable("draftId") Integer draftId,
                                      Principal principal) {
            return partyDraftService.deletePartyDraftModel(principal, draftId);
        }

        @Operation(summary = "Update a party's draft",
            description = "if draft doesn't belong to authenticated user then method returns " +
                    "an error message and 403 status")
        @PatchMapping("/{draftId}")
        public HttpStatus updateDraft(@PathVariable("draftId") Integer draftId,
                                      @RequestBody PartyDraftDto partyDraftDto,
                                      Principal principal) {
            return partyDraftService.updatePartyDraftModel(principal,
                                                            draftId,
                                                            partyDraftDto);
        }
}