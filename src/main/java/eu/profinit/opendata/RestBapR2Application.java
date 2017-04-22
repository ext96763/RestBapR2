package eu.profinit.opendata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan
@EnableSwagger2
public class RestBapR2Application {

	public static void main(String[] args) {
		SpringApplication.run(RestBapR2Application.class, args);
	}
}
