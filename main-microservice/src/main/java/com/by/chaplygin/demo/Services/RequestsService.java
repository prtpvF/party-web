package com.by.chaplygin.demo.Services;

import com.by.chaplygin.demo.Model.Person;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestsService {

private final RestTemplate restTemplate;

    public void sendRequestToEmailService(String email, int partyId,  String subject, String type){

        String serviceName = "gateway/";
        String endpoint = "email-microservice/email/send";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();

        params.add("email", email);
        params.add("subject", subject);
        params.add("type", type);
        params.add("partyId", partyId);



    }

    public void sendRequestToEmailService(String email, String city,  String subject, String body){


        String serviceName = "gateway-server";
        String endpoint = "/email/send/create";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();

        params.add("email", email);
        params.add("subject", subject);
        params.add("body", body);

        params.add("city", city);


        ResponseEntity<Void> response = restTemplate.postForEntity("http://"+ serviceName + endpoint, params, Void.class);

    }
}
