package com.email.email.microservice.Controllers;

import com.email.email.microservice.EmailSamples.SamplesPars;
import com.email.email.microservice.EmailSamples.Type;
import com.email.email.microservice.Model.EmailParams;
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


    //todo - конвертирование файлов
    @PostMapping("/send")
    private HttpStatus sendEmail(@RequestBody EmailParams emailParams) {

        emailSenderService.sendEmail(emailParams);

        return HttpStatus.OK;
    }



}
