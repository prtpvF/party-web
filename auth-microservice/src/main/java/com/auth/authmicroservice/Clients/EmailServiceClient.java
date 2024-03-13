package com.auth.authmicroservice.Clients;

import com.netflix.appinfo.InstanceInfo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailServiceClient {

    private final DiscoveryClient discoveryClient;

    @Autowired
    public EmailServiceClient(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }


    public String getServiceUrl(String serviceName) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        if (instances.isEmpty()) {
            throw new RuntimeException("No instances found for service: " + serviceName);
        }

        ServiceInstance instance = instances.get(0);
        if(instance.)
        return instance.getUri().toString();
    }
}