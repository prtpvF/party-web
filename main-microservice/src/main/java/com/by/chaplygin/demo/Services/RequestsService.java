package com.by.chaplygin.demo.Services;

import com.by.chaplygin.demo.Model.EmailParams;
import com.by.chaplygin.demo.Model.Person;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
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



    public HttpStatusCode sendRequestToMailService(EmailParams emailParams){
        String serviceName = "gateway-server/";
        String endpoint = "email/send";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();



        ResponseEntity<Void> response = restTemplate.postForEntity("http://"+ serviceName + endpoint, emailParams, Void.class);
        return response.getStatusCode();
    }
}
