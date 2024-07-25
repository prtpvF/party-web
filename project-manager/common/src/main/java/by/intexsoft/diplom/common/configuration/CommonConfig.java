package by.intexsoft.diplom.common.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = "by.intexsoft.diplom.common")
@Configuration
@EntityScan(basePackages = "by.intexsoft.diplom.common.model")
@EnableJpaRepositories(basePackages = "by.intexsoft.diplom.common.repository")
public class CommonConfig {


}
