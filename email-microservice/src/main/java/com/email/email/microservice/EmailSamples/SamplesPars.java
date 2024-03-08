package com.email.email.microservice.EmailSamples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

@Service
public class SamplesPars {

    public String getSampleText(Type type) {
        String message = "";
        switch (type) {
            case CREATE_PARTY:
                message = getText("src/main/java/com/email/email/microservice/EmailSamples/CreatePartySample");
                break;
            case DELETE_ACCOUNT:
                message = getText("src/main/java/com/email/email/microservice/EmailSamples/DeleteAccountSample");
                break;
            case DELETE_PARTY:
                message = getText("src/main/java/com/email/email/microservice/EmailSamples/DeletePartySample");
                break;
            case REGISTRATION:
                message = getText("src/main/java/com/email/email/microservice/EmailSamples/RegistrationSample");
                break;
        }
        return message;
    }

    private String getText(String filePath) {
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        } catch (IOException e) {

        }
        return content;
    }
}
