package by.intexsoft.diplom.publicapi.controller;

import by.intexsoft.diplom.publicapi.dto.PasswordResetDto;
import by.intexsoft.diplom.publicapi.dto.PersonDto;
import by.intexsoft.diplom.publicapi.dto.PersonUpdateDto;
import by.intexsoft.diplom.publicapi.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
    public ResponseEntity resetPassword(@RequestHeader(value = "token", required = false) String token,
                                        @Valid @RequestBody PasswordResetDto passwordResetDto,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        personService.resetPassword(passwordResetDto, token);
        return new ResponseEntity("success", HttpStatus.OK);
    }

    @DeleteMapping("/account")
    public HttpStatus deleteAccount(@RequestHeader(value = "token") String token) {
        personService.deleteAccount(token);
        return HttpStatus.OK;
    }

    @PutMapping("/update")
    public HttpStatus updateProfile(@RequestHeader(value = "token") String token,
                                    @RequestBody PersonUpdateDto personUpdateDto) {
        personService.updateProfile(token, personUpdateDto);
        return HttpStatus.OK;
    }
}