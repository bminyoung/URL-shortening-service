package com.practice.urlShortening.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.linecorp.armeria.server.docs.DocService;
import com.linecorp.armeria.server.logging.AccessLogWriter;
import com.linecorp.armeria.server.logging.LoggingService;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;
import com.practice.urlShortening.Controller.HomeController;
import com.practice.urlShortening.Controller.UrlDecodeController;
import com.practice.urlShortening.Controller.UrlDeleteController;
import com.practice.urlShortening.Controller.UrlRedirectController;
import com.practice.urlShortening.Controller.UrlRegisterController;

@Configuration
public class UrlConfig {
	
	@Bean
	public ArmeriaServerConfigurator armeriaServerConfiguraor(HomeController homeController,
															  UrlDecodeController urlDecodeController,
															  UrlRegisterController urlRegisterController,
															  UrlRedirectController urlRedirectController,
															  UrlDeleteController urlDeleteController) {
		return builder -> {
            builder.serviceUnder("/docs", new DocService());
            builder.decorator(LoggingService.newDecorator());
            builder.accessLogWriter(AccessLogWriter.combined(), false);
            builder.annotatedService(homeController);
			builder.annotatedService(urlDecodeController);
			builder.annotatedService(urlRegisterController);
			builder.annotatedService(urlRedirectController);
			builder.annotatedService(urlDeleteController);
		};
	}

}
