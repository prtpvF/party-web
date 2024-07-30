package by.intexsoft.diplom.auth.controller;

import by.intexsoft.diplom.auth.dto.VerifyCodeDto;
import by.intexsoft.diplom.auth.service.EmailVerificationService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/verification")
public class EmailVerificationController {

        private final EmailVerificationService emailVerificationService;

        @PostMapping("/email")
        public HttpStatus verifyEmail(@RequestBody VerifyCodeDto verifyCodeDto) {
            return emailVerificationService.doCodesMatch(verifyCodeDto);
        }
}
