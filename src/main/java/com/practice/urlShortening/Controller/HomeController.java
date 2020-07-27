package com.practice.urlShortening.Controller;

import org.springframework.stereotype.Controller;

import com.linecorp.armeria.server.annotation.ExceptionHandler;
import com.linecorp.armeria.server.annotation.Get;
import com.practice.urlShortening.Url.UrlExceptionHandler;

@Controller
@ExceptionHandler(UrlExceptionHandler.class)
public class HomeController {
	
	@Get("/")
	public String Main() {
		return "This is Main\n"
				+ "/urls/{shortUrl} : get original Url\n";
	}
}
