package io.spring.sample.matchmaking;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class OpenTelemetryConfiguration {

	@Bean
	InstallOpenTelemetryAppender installOpenTelemetryAppender(OpenTelemetry openTelemetry) {
		return new InstallOpenTelemetryAppender(openTelemetry);
	}

	class InstallOpenTelemetryAppender implements InitializingBean {

		private final OpenTelemetry openTelemetry;

		InstallOpenTelemetryAppender(OpenTelemetry openTelemetry) {
			this.openTelemetry = openTelemetry;
		}

		@Override
		public void afterPropertiesSet() {
			OpenTelemetryAppender.install(this.openTelemetry);
		}
	}

}
