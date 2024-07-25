package by.intexsoft.diplom.person.controller;

import by.intexsoft.diplom.common_module.model.ParticipationRequest;
import by.intexsoft.diplom.person.dto.ParticipationRequestDto;
import by.intexsoft.diplom.person.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/person")
public class PersonController {

        private final PersonService personService;

        @PreAuthorize("hasRole('ROLE_USER')")
        @PostMapping("/participation/party/{id}")
        public HttpStatus sendParticipationRequest(@PathVariable("id") int partyId,
                                                   @RequestHeader("token") String token) {
            return personService.sendParticipationRequest(partyId, token);
        }

        @PreAuthorize("hasRole('ROLE_USER')")
        @GetMapping("/participation/requests")
        public List<ParticipationRequestDto> getPersonParticipationRequest(@RequestHeader("token") String token) {
                return personService.getAllPersonParticipationRequests(token);
        }

        @PreAuthorize("hasRole('ROLE_USER')")
        @GetMapping("/participation/request/{id}")
        public ParticipationRequestDto getParticipationRequest(@PathVariable("id") int participationRequestId,
                                                            @RequestHeader("token") String token) {
                return personService.getValidatedPersonParticipationRequest(participationRequestId);
        }

        @PreAuthorize("hasRole('ROLE_USER')")
        @DeleteMapping("/participate/request/{id}")
        public HttpStatus deleteParticipationRequest(@PathVariable("id") int participationRequestId,
                                                    @RequestHeader("token") String token){
                return personService.deleteParticipationRequest(participationRequestId, token);
        }
}