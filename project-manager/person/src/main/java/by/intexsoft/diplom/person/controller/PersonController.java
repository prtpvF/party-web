package by.intexsoft.diplom.person.controller;

import by.intexsoft.diplom.person.dto.ParticipationRequestDto;
import by.intexsoft.diplom.person.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.keycloak.authorization.client.util.Http;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/person")
public class PersonController {

        private final PersonService personService;

        @PostMapping("/participation/party/{id}")
        public HttpStatus sendParticipationRequest(@PathVariable("id") int partyId,
                                                   Principal principal) {
            return personService.sendParticipationRequest(partyId, principal);
        }

        @PatchMapping("/rate/party/{id}")
        public HttpStatus rateParty(@PathVariable("id") Integer partyId,
                              @RequestParam("rate") Integer rate,
                                    Principal principal) {
                return personService.rateParty(partyId, rate, principal);
        }

        @GetMapping("/participation/requests")
        public List<ParticipationRequestDto> getPersonParticipationRequest(Principal principal) {
                return personService.getAllPersonParticipationRequests(principal);
        }

        @GetMapping("/participation/request/{id}")
        public ParticipationRequestDto getParticipationRequest(@PathVariable("id") int participationRequestId) {
                return personService.getValidatedPersonParticipationRequest(participationRequestId);
        }

        @DeleteMapping("/participate/request/{id}")
        public HttpStatus deleteParticipationRequest(@PathVariable("id") int participationRequestId,
                                                     Principal principal){
                return personService.deleteParticipationRequest(participationRequestId, principal);
        }
}