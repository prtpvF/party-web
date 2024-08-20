package by.intexsoft.diplom.draft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
<<<<<<< HEAD

@SpringBootApplication
@ComponentScan(basePackages = "by.intexsoft.diplom")
=======
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(basePackages = "by.intexsoft.diplom")
@PropertySource("classpath:message.properties")
>>>>>>> 3fde4c30443d2041b900952d15a715083e9858cf
public class DraftApplication {

	public static void main(String[] args) {
		SpringApplication.run(DraftApplication.class, args);
	}

}
