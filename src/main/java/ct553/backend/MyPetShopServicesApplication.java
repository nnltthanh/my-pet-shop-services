package ct553.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableJpaAuditing
@Configuration
@Slf4j
public class MyPetShopServicesApplication {
	public static void main(String[] args) {
		log.info("Server of My pet shop is running...");
		SpringApplication.run(MyPetShopServicesApplication.class, args);
	}

}
