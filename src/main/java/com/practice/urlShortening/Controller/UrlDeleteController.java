package com.practice.urlShortening.Controller;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.server.annotation.Delete;
import com.linecorp.armeria.server.annotation.ExceptionHandler;
import com.linecorp.armeria.server.annotation.Param;
import com.practice.urlShortening.Response.SuccessResponse;
import com.practice.urlShortening.Response.UrlException.OtherException;
import com.practice.urlShortening.Response.UrlResponse.ResultCode;
import com.practice.urlShortening.Service.UrlService;
import com.practice.urlShortening.Url.UrlExceptionHandler;

@Controller
@ExceptionHandler(UrlExceptionHandler.class)
public class UrlDeleteController {
	
	private static final ObjectMapper mapper = new ObjectMapper();

	@Resource(name = "urlServiceImpl")
	private UrlService service;
	
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
}
