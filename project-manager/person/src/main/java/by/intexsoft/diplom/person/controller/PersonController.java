package by.intexsoft.diplom.person.controller;

import by.intexsoft.diplom.person.dto.ParticipationRequestDto;
import by.intexsoft.diplom.person.service.PersonService;
import lombok.RequiredArgsConstructor;
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

        @PreAuthorize("hasRole('ROLE_USER')")
        @PostMapping("/participation/party/{id}")
        public HttpStatus sendParticipationRequest(@PathVariable("id") int partyId,
                                                   Principal principal) {
            return personService.sendParticipationRequest(partyId, principal);
        }

        @PreAuthorize("hasRole('ROLE_USER')")
        @GetMapping("/participation/requests")
        public List<ParticipationRequestDto> getPersonParticipationRequest(Principal principal) {
                return personService.getAllPersonParticipationRequests(principal);
        }

        @PreAuthorize("hasRole('ROLE_USER')")
        @GetMapping("/participation/request/{id}")
        public ParticipationRequestDto getParticipationRequest(@PathVariable("id") int participationRequestId) {
                return personService.getValidatedPersonParticipationRequest(participationRequestId);
        }

        @PreAuthorize("hasRole('ROLE_USER')")
        @DeleteMapping("/participate/request/{id}")
        public HttpStatus deleteParticipationRequest(@PathVariable("id") int participationRequestId,
                                                     Principal principal){
                return personService.deleteParticipationRequest(participationRequestId, principal);
        }
}