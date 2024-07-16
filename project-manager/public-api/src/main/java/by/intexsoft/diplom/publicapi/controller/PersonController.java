package by.intexsoft.diplom.publicapi.controller;

import by.intexsoft.diplom.publicapi.dto.PasswordResetDto;
import by.intexsoft.diplom.publicapi.dto.PersonDto;
import by.intexsoft.diplom.publicapi.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/person")
public class PersonController {
    private final PersonService personService;

    @GetMapping("/find")
    public PersonDto getPerson(@RequestBody String username) {
        return personService.getPersonDto(username);
    }

    @PutMapping("/password")
    public HttpStatus resetPassword(@RequestBody PasswordResetDto passwordResetDto, @RequestHeader(value = "token", required = false) String token){
        personService.resetPassword(passwordResetDto, token);
        return HttpStatus.OK;
    }

    @DeleteMapping("/account")
    public void deleteAccount(@RequestHeader(value = "token") String token){
        personService.deleteAccount(token);
    }


}
