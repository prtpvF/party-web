package com.by.chaplygin.demo.Controllers;

import com.by.chaplygin.demo.Exceptions.PersonNotFoundException;
import com.by.chaplygin.demo.Model.Person;
import com.by.chaplygin.demo.Security.JwtUtil;
import com.by.chaplygin.demo.Services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PostMapping("/update")
    public HttpStatus updatePerson(@RequestHeader("Authorization") String token, @RequestBody Person person){
        String username = jwtUtil.validateTokenAndRetrieveClaim(token);
        personService.updatePerson(username,person);
        return HttpStatus.OK;
    }
    @PostMapping("/add/party")
    public HttpStatus addPartyToPerson(@RequestHeader("Authorization") String token, @RequestBody Map<String, Integer> reauest){
        int id = reauest.get("id");
        String username = jwtUtil.validateTokenAndRetrieveClaim(token);
        personService.addPartyToPerson(username, id);
        return HttpStatus.OK;
    }
    @PostMapping("/convert/to/organizer")
    public HttpStatus changeRoleToOrganizer(@RequestHeader("Authorization") String token){
        String username = jwtUtil.validateTokenAndRetrieveClaim(token);
        personService.convertPersonToOrganizer(username);
        return HttpStatus.OK;
    }

    @PostMapping("/create/report")
    public HttpStatus createReport(@RequestBody Map<String, String> data){
        int personId = Integer.parseInt(data.get("personId"));
        int partyId = Integer.parseInt(data.get("partyId"));
        String text = data.get("text");
        HttpStatus status = personService.createReport(personId,partyId,text);
        return status;
    }


}
