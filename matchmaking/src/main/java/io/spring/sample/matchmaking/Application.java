package io.spring.sample.matchmaking;

import io.micrometer.observation.ObservationRegistry;
import io.spring.sample.matchmaking.observability.TraceResponseHeaderObservationFilter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public FilterRegistrationBean<TraceResponseHeaderObservationFilter> traceResponseFilter(ObservationRegistry registry) {
		return new FilterRegistrationBean<>(new TraceResponseHeaderObservationFilter(registry));
	}

}
