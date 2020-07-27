package com.practice.urlShortening.Controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Resource;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.server.annotation.ExceptionHandler;
import com.linecorp.armeria.server.annotation.Post;
import com.practice.urlShortening.Response.SuccessResponse;
import com.practice.urlShortening.Response.UrlException.DuplicatedException;
import com.practice.urlShortening.Response.UrlException.NotExistException;
import com.practice.urlShortening.Response.UrlException.OtherException;
import com.practice.urlShortening.Response.UrlResponse.ResultCode;
import com.practice.urlShortening.Service.UrlService;
import com.practice.urlShortening.Url.Url;
import com.practice.urlShortening.Url.UrlExceptionHandler;

@Controller
@ExceptionHandler(UrlExceptionHandler.class)
public class UrlRegisterController {
	
	private static final ObjectMapper mapper = new ObjectMapper();

	@Resource(name = "urlServiceImpl")
	private UrlService service;
	
	@Post("/urls")
	public CompletableFuture<HttpResponse> register(String originalUrl) throws JsonProcessingException {
		HashMap<String, String> map = mapper.readValue(originalUrl, HashMap.class);
		Url url = new Url(map.get("originalUrl"));
		UrlValidator urlVal = new UrlValidator();
		if(!urlVal.isValid(url.getOriginalUrl())) {
			throw new NotExistException("not valid url");
		}
		CompletableFuture<Long> res = service.registerUrl(url);
		return res.thenApply(result -> {
			if(result > 0) {
				map.clear();
				map.put("shortUrl", url.getShortUrl());
				try {
					return HttpResponse.of(HttpStatus.OK, MediaType.JSON_UTF_8, mapper.writeValueAsBytes(
							new SuccessResponse(map,
											  ResultCode.SUCCESS,
											  Instant.now().toString())));
				} catch (JsonProcessingException e) {
					throw new OtherException("json processing exception");
				}
			}else{
				throw new DuplicatedException("url already exist");
			}
		});
	}
}
