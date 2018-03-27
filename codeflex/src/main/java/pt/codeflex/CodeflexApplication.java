package pt.codeflex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
@EnableJpaRepositories("pt.codeflex.repositories")
public class CodeflexApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeflexApplication.class, args);
	}
}
