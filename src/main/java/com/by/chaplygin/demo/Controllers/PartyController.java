package com.by.chaplygin.demo.Controllers;

import com.by.chaplygin.demo.Exceptions.PersonNotFoundException;
import com.by.chaplygin.demo.Model.Party;
import com.by.chaplygin.demo.Model.Person;
import com.by.chaplygin.demo.Security.JwtUtil;
import com.by.chaplygin.demo.Services.PartyServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/party")
public class PartyController {
    private final PartyServices partyServices;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    public HttpStatus createParty(@RequestHeader("Authorization") String token, @RequestBody Party party){
        String username = jwtUtil.validateTokenAndRetrieveClaim(token);
        try {
            partyServices.createParty(username, party);
        } catch (PersonNotFoundException e) {
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.OK;
    }


    @GetMapping("/partys")
    public List<Party> findAllParty(){
        return partyServices.findAllParty();
    }
}
