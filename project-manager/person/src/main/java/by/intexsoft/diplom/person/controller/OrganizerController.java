package by.intexsoft.diplom.person.controller;

import by.intexsoft.diplom.common_module.model.ParticipationRequest;
import by.intexsoft.diplom.person.dto.OrgAnswerDto;
import by.intexsoft.diplom.person.dto.PartyDto;
import by.intexsoft.diplom.person.service.OrganizerService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/organizer")
public class OrganizerController {

        private final OrganizerService organizerService;

        @PreAuthorize("hasRole('ROLE_ORGANIZER')")
        @PostMapping("/party")
        public HttpStatus createParty(@RequestBody PartyDto partyDto,
                                      @RequestHeader("token") String token){
            return organizerService.createPartyRequest(token, partyDto);
        }

        @PreAuthorize("hasRole('ROLE_ORGANIZER')")
        @DeleteMapping("/party/{id}")
        public HttpStatus createDeletingPartyRequest(@PathVariable("id") int partyId,
                                                     @RequestHeader("token") String token){
            return organizerService.createPartyDeleteRequest(partyId, token);
        }

        @PreAuthorize("hasRole('ROLE_ORGANIZER')")
        @PatchMapping("/party/{id}")
        public HttpStatus updateParty(@PathVariable("id") int partyId,
                                      @RequestHeader("token") String token,
                                      @RequestBody PartyDto partyDto) {
            return organizerService.updateParty(partyId, token, partyDto);
        }

        @PostMapping("/request/{id}")
        public HttpStatus answerParticipationRequest(@PathVariable("id") int requestId,
                                                     @RequestHeader("token") String token,
                                                     @RequestBody OrgAnswerDto orgAnswerDto){
                return organizerService.answerRequest(requestId, token, orgAnswerDto);
        }

        @PreAuthorize("hasRole('ROLE_ORGANIZER')")
        @DeleteMapping("/participation-request/{id}")
        public HttpStatus deleteParticipationRequest(@PathVariable("id") int requestId,
                                                     @RequestHeader("token") String token){
              return organizerService.deleteRequest(requestId, token);
        }
}
