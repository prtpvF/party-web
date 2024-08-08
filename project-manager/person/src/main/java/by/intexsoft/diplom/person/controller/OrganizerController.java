package by.intexsoft.diplom.person.controller;

import by.intexsoft.diplom.person.dto.OrgAnswerDto;
import by.intexsoft.diplom.person.dto.PartyDto;
import by.intexsoft.diplom.person.service.OrganizerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/organizer")
public class OrganizerController {

        private final OrganizerService organizerService;

        @PreAuthorize("hasRole('ROLE_ORGANIZER')")
        @PostMapping("/party")
        public HttpStatus createParty(@RequestBody PartyDto partyDto,
                                      Principal principal){
            return organizerService.createPartyRequest(principal, partyDto);
        }

        @PreAuthorize("hasRole('ROLE_ORGANIZER')")
        @DeleteMapping("/party/{id}")
        public HttpStatus createDeletingPartyRequest(@PathVariable("id") int partyId,
                                                     Principal principal){
            return organizerService.createPartyDeleteRequest(partyId, principal);
        }

        @PreAuthorize("hasRole('ROLE_ORGANIZER')")
        @PatchMapping("/party/{id}")
        public HttpStatus updateParty(@PathVariable("id") int partyId,
                                      Principal principal,
                                      @RequestBody PartyDto partyDto) {
            return organizerService.updateParty(partyId, principal, partyDto);
        }

        @PostMapping("/request/{id}")
        public HttpStatus answerParticipationRequest(@PathVariable("id") int requestId,
                                                     Principal principal,
                                                     @RequestBody OrgAnswerDto orgAnswerDto){
                return organizerService.answerRequest(requestId, principal, orgAnswerDto);
        }

        @PreAuthorize("hasRole('ROLE_ORGANIZER')")
        @DeleteMapping("/participation-request/{id}")
        public HttpStatus deleteParticipationRequest(@PathVariable("id") int requestId,
                                                     Principal principal){
              return organizerService.deleteRequest(requestId, principal);
        }
}