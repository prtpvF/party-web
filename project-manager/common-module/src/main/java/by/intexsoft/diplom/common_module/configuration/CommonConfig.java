package by.intexsoft.diplom.common_module.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = "by.intexsoft.diplom.common_module")
@Configuration
@EntityScan(basePackages = "by.intexsoft.diplom.common_module.model")
@EnableJpaRepositories(basePackages = "by.intexsoft.diplom.common_module.repository")
public class CommonConfig {

}
