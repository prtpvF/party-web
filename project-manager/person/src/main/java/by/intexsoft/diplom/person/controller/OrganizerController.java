package by.intexsoft.diplom.person.controller;

import by.intexsoft.diplom.person.dto.PartyCreateDto;
import by.intexsoft.diplom.person.service.OrganizerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/organizer")
public class OrganizerController {
    private final OrganizerService organizerService;

    @PostMapping("/party")
    public HttpStatus createParty(@ModelAttribute PartyCreateDto partyCreateDto,
                                  @RequestHeader("token") String token){
        return organizerService.createParty(token, partyCreateDto);

    }
}
