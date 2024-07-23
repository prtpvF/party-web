package by.intexsoft.diplom.person.controller;

import by.intexsoft.diplom.person.dto.PartyDto;
import by.intexsoft.diplom.person.service.OrganizerService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/organizer")
public class OrganizerController {

        private final OrganizerService organizerService;

        @PostMapping("/party")
        public HttpStatus createParty(@RequestBody PartyDto partyDto,
                                      @RequestHeader("token") String token){
            return organizerService.createParty(token, partyDto);
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
                                      @RequestBody PartyDto partyDto){
            return organizerService.updateParty(partyId, token, partyDto);
        }
}
