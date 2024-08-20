package by.intexsoft.diplom.draft.configuration;

import org.modelmapper.ModelMapper;
<<<<<<< HEAD
import org.modelmapper.convention.MatchingStrategies;
=======
>>>>>>> 3fde4c30443d2041b900952d15a715083e9858cf
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DraftSpringConfiguration {

        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }
}
