package com.email.email.microservice.Controllers;

import com.email.email.microservice.EmailSamples.SamplesPars;
import com.email.email.microservice.EmailSamples.Type;
import com.email.email.microservice.Services.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EmailSenderController {
    private final EmailSenderService emailSenderService;
    private final SamplesPars samplesPars;

    //todo - конвертирование файлов
    @PostMapping("/send")
    public HttpStatus sendEmail(@RequestParam("email") String email,
                                @RequestParam("subject") String subject,
                                @RequestParam("type") String type,
                                @RequestParam(value = "partyId", required = false) Integer partyId,
                                @RequestParam(value = "requestId", required = false) Integer requestId) {
        String message = samplesPars.getSampleText(Type.valueOf(type));
        if (partyId != null && requestId != null) {
            message = samplesPars.getSampleTextWithParams(Type.valueOf(type), partyId, requestId);
        }
        emailSenderService.sendEmail(email, subject, message);

        return HttpStatus.OK;
    }

    @PostMapping("/send/create")
    public HttpStatus sendEmail(@RequestParam("email") String email,
                                @RequestParam("subject") String subject,
                                @RequestParam("body") String body) {

        emailSenderService.sendEmail(email, subject, body);

        return HttpStatus.OK;
    }


}
