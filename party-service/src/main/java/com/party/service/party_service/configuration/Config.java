package com.party.service.party_service.configuration;

import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

public class Config extends ElasticsearchConfiguration {
    @Override
    public ClientConfiguration clientConfiguration() {
        return null;
    }
}
