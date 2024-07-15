package by.intexsoft.diplom.publicapi.controller;

import by.intexsoft.diplom.publicapi.dto.PartyDto;
import by.intexsoft.diplom.publicapi.dto.PersonDto;
import by.intexsoft.diplom.publicapi.service.PublicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {
    private final PublicService publicService;

    @GetMapping("/parties/{city}")
    public List<PartyDto> findAllByCity(@PathVariable("city") String city) {
        return publicService.getPartyInPersonCity(city);
    }

    @GetMapping("/party/{id}")
    public PartyDto getParty(@PathVariable("id") int id) {
        return publicService.getParty(id);
    }

    @GetMapping("/person/{id}")
    public PersonDto getPerson(@PathVariable("id") int id){
        return publicService.getPerson(id);
    }


}
