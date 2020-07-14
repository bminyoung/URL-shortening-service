package com.practice.urlShortening.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.linecorp.armeria.server.docs.DocService;
import com.linecorp.armeria.server.logging.AccessLogWriter;
import com.linecorp.armeria.server.logging.LoggingService;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;
import com.practice.urlShortening.Controller.UrlController;

@Configuration
public class UrlConfig {
	
	@Bean
	public ArmeriaServerConfigurator armeriaServerConfiguraor(UrlController service) {
		return builder -> {
            builder.serviceUnder("/docs", new DocService());
            builder.decorator(LoggingService.newDecorator());
            builder.accessLogWriter(AccessLogWriter.combined(), false);
			builder.annotatedService(service);
		};
	}

}
