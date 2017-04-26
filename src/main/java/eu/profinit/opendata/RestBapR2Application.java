package eu.profinit.opendata;

import eu.profinit.opendata.ipfilter.IpLimitFilter;
import eu.profinit.opendata.ipfilter.IpTimeWindowManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;


@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class RestBapR2Application {

	public static void main(String[] args) {
		SpringApplication.run(RestBapR2Application.class, args);
	}

	@Bean
	@Scope(value="singleton")
	@Order(Ordered.LOWEST_PRECEDENCE)
	public IpTimeWindowManager ipTimeWindowManager() {
		return new IpTimeWindowManager();
	}

	@Bean
	@Order(Ordered.LOWEST_PRECEDENCE)
	public IpLimitFilter ipLimitFilter(IpTimeWindowManager ipTimeWindowManager) {
		return new IpLimitFilter(ipTimeWindowManager);
	}

	@Bean
	@Order(Ordered.LOWEST_PRECEDENCE)
	public FilterRegistrationBean ipLimitFilterRegistrationBean(IpLimitFilter ipLimitFilter) {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(ipLimitFilter);
		registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
		return registrationBean;
	}
}
