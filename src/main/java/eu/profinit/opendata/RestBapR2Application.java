package eu.profinit.opendata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;



@SpringBootApplication
@ComponentScan
public class RestBapR2Application {

	public static void main(String[] args) {
		SpringApplication.run(RestBapR2Application.class, args);
	}
}
