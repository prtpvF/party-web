package by.intexsoft.diplom.person.configuration;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

        @Bean
        public ModelMapper modelMapper(){
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setSkipNullEnabled(true);
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            return modelMapper;
        }
}
