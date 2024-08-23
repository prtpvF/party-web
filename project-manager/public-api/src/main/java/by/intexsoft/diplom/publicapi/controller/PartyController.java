package by.intexsoft.diplom.publicapi.controller;

import by.intexsoft.diplom.publicapi.dto.PartyDto;
import by.intexsoft.diplom.publicapi.service.PartyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/party")
@Tag(name = "Party Controller", description = "Controller with public endpoints")
public class PartyController {

        private final PartyService partyService;

        private static final int DEFAULT_PAGE = 0;
        private static final int DEFAULT_SIZE = 20;
        private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "name");

        @Operation(
                summary = "find party by city",
                description = "after successfully login, application will retrieve person's ip" +
                        "and return party in person's city"
        )
        @GetMapping("/all/in/my")
        public Page<PartyDto> findAllInCityByIp(@RequestHeader(value = "X-Forwarded-For", required = false) String ip,
                                                Pageable pageable,
                                                Principal principal) {
            return partyService.getPartyInPersonCityByIp(ip, principal, pageable);
        }

        @GetMapping("/all/by/name")
        public Page<PartyDto> findPartiesByName(@RequestParam("name") String name,
                                                Pageable pageable) {
                return partyService.getPartiesByName(name, pageable);
        }

        @GetMapping("/all/by/type")
        public Page<PartyDto> findPartiesByType(@RequestParam("type") String type,
                                                Pageable pageable) {
                return partyService.getPartiesByType(type, pageable);
        }

        @GetMapping("/all/{city}")
        public Page<PartyDto> findAllByCity(@PathVariable("city") String city,
                                             Pageable pageable) {
                return partyService.getPartyByCity(city, pageable);
        }

        @GetMapping("/{id}")
        public PartyDto getParty(@PathVariable("id") int id) {
            return partyService.getPartyById(id);
        }
}