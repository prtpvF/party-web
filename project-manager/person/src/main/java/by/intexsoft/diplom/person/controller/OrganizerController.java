package by.intexsoft.diplom.person.controller;

import by.intexsoft.diplom.person.dto.OrgAnswerDto;
import by.intexsoft.diplom.person.dto.PartyDto;
import by.intexsoft.diplom.person.service.OrganizerService;
import by.intexsoft.diplom.person.service.request.CrudPartyRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/organizer")
public class OrganizerController {

        private final OrganizerService organizerService;
        private final CrudPartyRequestService crudPartyRequestService;

        @PostMapping("/party")
        public HttpStatus createParty(@RequestBody PartyDto partyDto,
                                      Principal principal){
            return crudPartyRequestService.createPartyRequest(principal, partyDto);
        }

        @DeleteMapping("/party/{id}")
        public HttpStatus deletePartyRequest(@PathVariable("id") int partyId,
                                                     Principal principal){
            return crudPartyRequestService.createPartyDeleteRequest(partyId, principal);
        }

//        @PreAuthorize("hasRole('ROLE_ORGANIZER')")
        @PatchMapping("/party/{id}")
        public HttpStatus updateParty(@PathVariable("id") int partyId,
                                      Principal principal,
                                      @RequestBody PartyDto partyDto) {

            return crudPartyRequestService.updateParty(partyId, principal, partyDto);
        }

        @PostMapping("/request/{id}")
        public HttpStatus answerParticipationRequest(@PathVariable("id") int requestId,
                                                     Principal principal,
                                                     @RequestBody OrgAnswerDto orgAnswerDto){
                return organizerService.answerRequest(requestId, principal, orgAnswerDto);
        }

        @DeleteMapping("/participation-request/{id}")
        public HttpStatus deleteParticipationRequest(@PathVariable("id") int partyId,
                                                     Principal principal){
              return crudPartyRequestService.createPartyDeleteRequest(partyId, principal);
        }

        //todo my party
        public List<PartyDto> getAllMyParties(Principal principal) {
                return organizerService.getMyParties();
        }
}