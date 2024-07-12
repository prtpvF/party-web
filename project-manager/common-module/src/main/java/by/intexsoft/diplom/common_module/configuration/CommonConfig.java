package by.intexsoft.diplom.common_module.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = "com.common.module.common_module")
@Configuration
@EntityScan(basePackages = "com.common.module.common_module.models")
@EnableJpaRepositories(basePackages = "com.common.module.common_module.repository")
public class CommonConfig {

}
