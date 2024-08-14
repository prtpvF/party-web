package by.intexsoft.diplom.admin.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminSpringConfiguration {

        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }
}
