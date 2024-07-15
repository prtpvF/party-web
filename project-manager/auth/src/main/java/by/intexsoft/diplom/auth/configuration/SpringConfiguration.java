package by.intexsoft.diplom.auth.configuration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class SpringConfiguration {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }



}
