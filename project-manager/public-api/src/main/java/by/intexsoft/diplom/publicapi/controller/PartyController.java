package by.intexsoft.diplom.publicapi.controller;

import by.intexsoft.diplom.common_module.repository.PartyRepository;
import by.intexsoft.diplom.publicapi.dto.PartyDto;
import by.intexsoft.diplom.publicapi.service.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/party")
public class PartyController {
    private final PartyService partyService;


    @GetMapping("/all/{city}")
    public List<PartyDto> findAllByCity(@PathVariable("city") String city) {
        return partyService.getPartyInPersonCity(city);
    }

    @GetMapping("/{id}")
    public PartyDto getParty(@PathVariable("id") int id) {
        return partyService.getParty(id);
    }

}
