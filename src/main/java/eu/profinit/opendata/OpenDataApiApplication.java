package eu.profinit.opendata;

import eu.profinit.opendata.ipfilter.IpLimitFilter;
import eu.profinit.opendata.ipfilter.IpTimeWindowManager;
import eu.profinit.opendata.throttlingfilter.ThrottlingFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;


/**
 *Main application class. Beans conf.
 */

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class OpenDataApiApplication extends SpringBootServletInitializer{

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(applicationClass);
    }

    private static Class<OpenDataApiApplication> applicationClass = OpenDataApiApplication.class;


	public static void main(String[] args) throws Exception {
		SpringApplication.run(OpenDataApiApplication.class, args);
	}

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory factory =
                new TomcatEmbeddedServletContainerFactory();
        return factory;
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