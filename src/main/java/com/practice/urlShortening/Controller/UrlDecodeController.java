package com.practice.urlShortening.Controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.server.annotation.ExceptionHandler;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Param;
import com.practice.urlShortening.Response.SuccessResponse;
import com.practice.urlShortening.Response.UrlResponse.ResultCode;
import com.practice.urlShortening.Response.UrlException.*;
import com.practice.urlShortening.Service.UrlService;
import com.practice.urlShortening.Url.Url;
import com.practice.urlShortening.Url.UrlExceptionHandler;

@Controller
@ExceptionHandler(UrlExceptionHandler.class)
public class UrlDecodeController {
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	@Resource(name = "urlServiceImpl")
	private UrlService service;
	
	/*
	 * url exists : return url
	 * not exists : return with fail msg
	 * */
	@Get("/urls/{shortUrl}")
	public CompletableFuture<HttpResponse> get(@Param String shortUrl) throws JsonProcessingException{
		CompletableFuture<Url> urlInfo = service.getOriginalUrl(shortUrl);
		return urlInfo.thenApply(url -> {
			if(url == null) {
				throw new NotExistException("there is no url information");
			}else {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("originalUrl", url.getOriginalUrl());
				try {
					return HttpResponse.of(HttpStatus.OK, MediaType.JSON_UTF_8, mapper.writeValueAsBytes(
							new SuccessResponse(map,
											  ResultCode.SUCCESS,
											  Instant.now().toString())));
				} catch (JsonProcessingException e) {
					throw new OtherException("json processing exception");
				}
			}
		});
	}
}
