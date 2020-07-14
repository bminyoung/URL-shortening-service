package com.practice.urlShortening.Controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import javax.management.RuntimeErrorException;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.armeria.common.HttpHeaderNames;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.common.ResponseHeaders;
import com.linecorp.armeria.server.annotation.Delete;
import com.linecorp.armeria.server.annotation.ExceptionHandler;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Param;
import com.linecorp.armeria.server.annotation.Post;
import com.practice.urlShortening.Response.SuccessResponse;
import com.practice.urlShortening.Response.UrlResponse.ResultCode;
import com.practice.urlShortening.Response.UrlException.*;
import com.practice.urlShortening.Service.UrlService;
import com.practice.urlShortening.Url.Url;
import com.practice.urlShortening.Url.UrlExceptionHandler;

@Controller
@ExceptionHandler(UrlExceptionHandler.class)
public class UrlController {
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	private UrlService service;
	
	@Get("/")
	public String Main() {
		return "This is Main\n"
				+ "/urls/{shortUrl} : get original Url\n";
	}
	
	/*
	 * url exists : return url
	 * not exists : return with fail msg
	 * */
	@Get("/urls/{shortUrl}")
	public CompletableFuture<HttpResponse> get(@Param String shortUrl) throws JsonProcessingException{
		CompletableFuture<Url> urlInfo = service.getUrl(shortUrl);
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
	
	/*
	 * success : return to main
	 * fail (not exists ...) : return with fail msg
	 * */
	@Delete("/urls/{shortUrl}")
	public CompletableFuture<HttpResponse> delete(@Param String shortUrl) throws Exception {
		CompletableFuture<Integer> res = service.delete(shortUrl);
		return res.thenApply(result -> {
			if(result > 0) {
				try {
					return HttpResponse.of(HttpStatus.OK, MediaType.JSON_UTF_8, mapper.writeValueAsBytes(
							new SuccessResponse("",
											  ResultCode.SUCCESS,
											  Instant.now().toString())));
				}
				 catch (JsonProcessingException e) {
					throw new OtherException("json processing exception");
				}
			}
			else {
				throw new OtherException("delete fail");
			}
		});
	}
	
	/*
	 * url exists : redirect
	 * not exists : return
	 * */
	@Get("/{shortUrl}")
	public CompletableFuture<HttpResponse> redirect(@Param String shortUrl) throws JsonProcessingException {
		CompletableFuture<Url> urlInfo = service.getUrl(shortUrl);
		return urlInfo.thenApply(url -> {
			if(url == null) {
				throw new NotExistException("there is no url information");
			}
			return HttpResponse.of(ResponseHeaders.of(HttpStatus.MOVED_PERMANENTLY, HttpHeaderNames.LOCATION, url.getOriginalUrl()));
		});
	}
}
