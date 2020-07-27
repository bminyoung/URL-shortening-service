package com.practice.urlShortening.Controller;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.armeria.common.HttpHeaderNames;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.ResponseHeaders;
import com.linecorp.armeria.server.annotation.ExceptionHandler;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Param;
import com.practice.urlShortening.Response.UrlException.NotExistException;
import com.practice.urlShortening.Service.UrlService;
import com.practice.urlShortening.Url.Url;
import com.practice.urlShortening.Url.UrlExceptionHandler;

@Controller
@ExceptionHandler(UrlExceptionHandler.class)
public class UrlRedirectController {
	private static final ObjectMapper mapper = new ObjectMapper();

	@Resource(name = "urlServiceImpl")
	private UrlService service;
	
	/*
	 * url exists : redirect
	 * not exists : return
	 * */
	@Get("/{shortUrl}")
	public CompletableFuture<HttpResponse> redirect(@Param String shortUrl) throws JsonProcessingException {
		CompletableFuture<Url> urlInfo = service.getOriginalUrl(shortUrl);
		return urlInfo.thenApply(url -> {
			if(url == null) {
				throw new NotExistException("there is no url information");
			}
			return HttpResponse.of(ResponseHeaders.of(HttpStatus.MOVED_PERMANENTLY, HttpHeaderNames.LOCATION, url.getOriginalUrl()));
		});
	}
}
