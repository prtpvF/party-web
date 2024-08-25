package by.intexsoft.diplom.publicapi.controller;

import by.intexsoft.diplom.publicapi.dto.PersonDto;
import by.intexsoft.diplom.publicapi.dto.PersonUpdateDto;
import by.intexsoft.diplom.publicapi.dto.UpdateRequestDto;
import by.intexsoft.diplom.publicapi.service.PersonService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/person")
@Tag(name = "Party Controller", description = "Controller with public endpoints")
public class PersonController {

        private final PersonService personService;

        @GetMapping("/find")
        public PersonDto getPerson(@RequestBody String username) {
            return personService.getPersonDto(username);
        }

        @DeleteMapping("/account")
        public HttpStatus deleteAccount(Principal principal) {
            personService.deleteAccount(principal);
            return HttpStatus.OK;
        }

        @PutMapping("/update")
        public HttpStatus updateProfile(Principal principal,
                                        @RequestBody UpdateRequestDto updateRequestDto) {
            personService.updateProfile(principal, updateRequestDto);
            return HttpStatus.OK;
        }

}