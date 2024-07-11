package com.by.chaplygin.demo.Configuration;


import com.by.chaplygin.demo.Util.JdbcRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AppConfig {

    @Bean
    public JdbcRequest jdbcRequest(JdbcTemplate jdbcTemplate) {
        return new JdbcRequest();
    }
}
