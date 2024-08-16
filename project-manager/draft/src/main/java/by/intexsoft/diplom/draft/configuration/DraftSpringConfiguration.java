package by.intexsoft.diplom.draft.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DraftSpringConfiguration {

        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }
}
