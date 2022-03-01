package id.zeemotion.showroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(ShowroomApplication.class);
		application.setWebApplicationType(WebApplicationType.NONE);
		application.run(args);
	}

}
