package by.intexsoft.diplom.admin.controller;

import by.intexsoft.diplom.admin.dto.PartyDto;
import by.intexsoft.diplom.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * <h3><b>if a method accepts Principal in parameters
 * than it means that method returns founded data in authorized person city</h3></b>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

        private final AdminService adminService;

        @GetMapping("/all/requests")
        public List<PartyDto> findAllPartyRequests(Principal principal) {
              return adminService.findAllPartyCrudRequestInAdminCity(principal);
        }

        @GetMapping("all/requests/by/{statusId}")
        public Page<PartyDto> findAllPartyRequestByStatus(@PathVariable("statusId") Integer statusId,
                                                          Principal principal,
                                                          Pageable pageable) {
               return adminService.findAllPartyRequestsByStatusAndCity(principal, statusId, pageable);
        }

        @GetMapping("/party/{id}")
        public PartyDto getPartyById(@PathVariable("id") Integer id) {
                return adminService.getParty(id);
        }

        @PostMapping("/request/{id}")
        public HttpStatus answerRequest(@PathVariable("id") Integer statusId,
                                        @RequestParam("flag") Boolean flag) {
                adminService.answerPartyRequest(statusId, flag);
                return HttpStatus.OK;
        }
}
