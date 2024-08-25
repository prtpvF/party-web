package by.intexsoft.diplom.person.controller;

import by.intexsoft.diplom.person.dto.OrgAnswerDto;
import by.intexsoft.diplom.person.dto.PartyDto;
import by.intexsoft.diplom.person.service.OrganizerService;
import by.intexsoft.diplom.person.service.request.CrudPartyRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/organizer")
public class OrganizerController {

        private final OrganizerService organizerService;
        private final CrudPartyRequestService crudPartyRequestService;

        @PostMapping(value = "/party", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
                                        MediaType.APPLICATION_OCTET_STREAM_VALUE})
        public HttpStatus createParty(@RequestPart("dto") PartyDto partyDto,
                                      Principal principal,
                                      @RequestPart("file") MultipartFile file){
            return crudPartyRequestService.createPartyRequest(principal, partyDto, file);
        }

        @DeleteMapping("/party/{id}")
        public HttpStatus deletePartyRequest(@PathVariable("id") int partyId,
                                                     Principal principal){
            return crudPartyRequestService.createPartyDeleteRequest(partyId, principal);
        }

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

        @GetMapping("/my-parties")
        public Page<PartyDto> getAllMyParties(Principal principal, Pageable pageable) {
                return organizerService.getMyParties(principal, pageable);
        }

}