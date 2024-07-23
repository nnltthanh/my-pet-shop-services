package ct553.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
// @EnableJpaAuditing
// @Configuration
@Slf4j
public class MyPetShopServicesApplication {
	public static void main(String[] args) {
		log.info("Server of fashion shop is running...");
		SpringApplication.run(MyPetShopServicesApplication.class, args);
	}

}
