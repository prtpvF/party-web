package com.auth.authmicroservice.Service;

import com.auth.authmicroservice.Clients.EmailServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.io.File;
import java.util.List;

@Service
public class RequestsServices {

    private final EmailServiceClient emailServiceClient;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public RequestsServices(EmailServiceClient emailServiceClient) {
        this.emailServiceClient = emailServiceClient;


    }


    public HttpStatusCode sendRequestToMailService(String email, String subject, List<File> attachments, String type){
        String url = emailServiceClient.getServiceUrl("email-microservice");
        String mailServ = url+"/email/send";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();

        params.add("email", email);
        params.add("subject", subject);
        params.add("type", type);

        ResponseEntity<Void> response = restTemplate.postForEntity(mailServ, params, Void.class);
        return response.getStatusCode();
    }


}
