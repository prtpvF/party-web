package com.by.chaplygin.demo.Controllers;

import com.by.chaplygin.demo.Exceptions.NotFoundException;
import com.by.chaplygin.demo.Security.JwtUtil;
import com.by.chaplygin.demo.Services.OrganizerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/organizer")
public class OrganizerController {
    private final OrganizerService organizerService;
    private final JwtUtil jwtUtil;

    @PostMapping("/accept/request")
    private HttpStatus acceptRequest(@RequestHeader("Authorization") String token,
                                    @RequestBody Map<String, Integer> request1){
        int partyId = request1.get("partyId");
        int requestId = request1.get("requestId");
        String username = jwtUtil.validateTokenAndRetrieveClaim(token);
        try {
            organizerService.acceptRequest(username, partyId,requestId);
        } catch (NotFoundException e) {
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.OK;
    }

    @PostMapping("/ban")
    private HttpStatus banGuest(@RequestHeader("Authorization") String token,
                                @RequestBody Map<String, String> data){
        String username = jwtUtil.validateTokenAndRetrieveClaim(token);
        int personId = Integer.parseInt(data.get("personId"));
        String endOfBan = data.get("endOfBan");
        organizerService.banGuest(personId,endOfBan,username);
        return HttpStatus.OK;
    }

}
