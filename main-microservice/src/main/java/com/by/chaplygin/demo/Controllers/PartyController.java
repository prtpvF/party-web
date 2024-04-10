package com.by.chaplygin.demo.Controllers;

import com.by.chaplygin.demo.Exceptions.PartyNotFoundException;
import com.by.chaplygin.demo.Exceptions.PersonNotFoundException;
import com.by.chaplygin.demo.Model.Party;
import com.by.chaplygin.demo.Model.Person;
import com.by.chaplygin.demo.Security.JwtUtil;
import com.by.chaplygin.demo.Services.PartyServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/party")
public class PartyController {
    private final PartyServices partyServices;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    private HttpStatus createParty(@RequestHeader("Authorization") String token, @RequestBody Party party){
        String username = jwtUtil.validateTokenAndRetrieveClaim(token);

            partyServices.createParty(username, party);

        return HttpStatus.OK;
    }
    @GetMapping("/all")
    private List<Party> findAllParty(){
        return partyServices.findAllParty();
    }

    @GetMapping("/find/by/property")
    private List<Party> findPartiesByProperty(@RequestBody Map<String, String> requestMap){
        String property = requestMap.get("property");
        String value = requestMap.get("value");
        List<Party> founded = partyServices.findByProperty(property,value);
        return founded;
    }
    @GetMapping("/page/{id}")
    private Party getParty(@RequestParam("id") int id) throws PartyNotFoundException {

        return partyServices.getParty(id);

    }

    @DeleteMapping("/delete/party")
    private HttpStatus deleteParty(@RequestBody Map<String, Integer> request){
        int id = request.get("id");
        partyServices.deleteParty(id);
        return HttpStatus.OK;
    }

    @GetMapping("/get/by/city")
    private Set<Party> getPartiesNearMe(@RequestBody String city){
        return  partyServices.getPartiesInMyCity(city);
    }



}
