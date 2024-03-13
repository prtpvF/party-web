package com.auth.authmicroservice.Service;

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


    @Autowired
    private RestTemplate restTemplate;




    public HttpStatusCode sendRequestToMailService(String email, String subject, List<File> attachments, String type){
        String serviceName = "email-microservice";
        String endpoint = "/email/send";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();

        params.add("email", email);
        params.add("subject", subject);
        params.add("type", type);

        ResponseEntity<Void> response = restTemplate.postForEntity("http://" + serviceName + endpoint, params, Void.class);
        return response.getStatusCode();
    }

}
