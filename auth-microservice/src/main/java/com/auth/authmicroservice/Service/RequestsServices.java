package com.auth.authmicroservice.Service;

import com.auth.authmicroservice.Model.EmailParams;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
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



    @CircuitBreaker(name = "email-microservice" )
    //@Bulkhead(name="bulkheadAuthMicroservice", type = Bulkhead.Type.THREADPOOL) //ограничение одновременных вызовов
    @Retry(name = "retryAuth-microservice")
    @RateLimiter(name = "auth-microservice") // Ограничение кол-во вызова в заданный момент времени
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
