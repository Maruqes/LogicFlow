package LogicFlow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("LogicFlow")
@EnableJpaRepositories("LogicFlow.repository")
public class LogicFlowApplication {
	public static void main(String[] args) {
		SpringApplication.run(LogicFlowApplication.class, args);
	}
}
