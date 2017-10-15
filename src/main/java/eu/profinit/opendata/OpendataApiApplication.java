package eu.profinit.opendata;

import eu.profinit.opendata.ipfilter.IpLimitFilter;
import eu.profinit.opendata.ipfilter.IpTimeWindowManager;
import eu.profinit.opendata.throttlingfilter.ThrottlingFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;


/**
 *Main application class. Beans configuration.
 */

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class OpendataApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpendataApiApplication.class, args);
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

	@Bean
	@Order(Ordered.LOWEST_PRECEDENCE)
	public ThrottlingFilter throttlingFilter(IpTimeWindowManager ipTimeWindowManager) {
		return new ThrottlingFilter(ipTimeWindowManager);
	}

	@Bean
	@Order(Ordered.LOWEST_PRECEDENCE)
	public FilterRegistrationBean throttlingFilterRegistrationBean(ThrottlingFilter throttlingFilter) {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(throttlingFilter);
		registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
		return registrationBean;
	}
}