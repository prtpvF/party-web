package by.intexsoft.diplom.publicapi.controller;

import by.intexsoft.diplom.publicapi.dto.PartyDto;
import by.intexsoft.diplom.publicapi.service.PartyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/party")
@Tag(name = "Party Controller", description = "Controller with public endpoints")
public class PartyController {

        private final PartyService partyService;

        @Operation(
                summary = "find party by city",
                description = "after successfully login, application will retrieve person's ip" +
                        "and return party in person's city"
        )
        @GetMapping("/all/{city}")
        public List<PartyDto> findAllByCity(@PathVariable("city") String city) {
            return partyService.getPartyInPersonCity(city);
        }

        @GetMapping("/{id}")
        public PartyDto getParty(@PathVariable("id") int id) {
            return partyService.getParty(id);
        }

}
