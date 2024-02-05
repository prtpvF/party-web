package com.by.chaplygin.demo.Controllers;

import com.by.chaplygin.demo.Exceptions.PersonNotFoundException;
import com.by.chaplygin.demo.Model.Person;
import com.by.chaplygin.demo.Security.JwtUtil;
import com.by.chaplygin.demo.Services.PersonService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;
    private final JwtUtil jwtUtil;

    @GetMapping("/index")
    public Person index(@RequestHeader("Authorization") String token) throws PersonNotFoundException {
        String username = jwtUtil.validateTokenAndRetrieveClaim(token);
        Person person = personService.indexPage(username);
        return person;
    }
    @DeleteMapping("/delete")
    public HttpStatus deleteAccount(@RequestHeader("Authorization") String token){
        String username = jwtUtil.validateTokenAndRetrieveClaim(token);
        personService.deleteAccount(username);
        return HttpStatus.OK;
    }
}