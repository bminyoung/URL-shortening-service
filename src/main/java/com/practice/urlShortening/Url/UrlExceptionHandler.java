package com.practice.urlShortening.Url;

import java.time.Instant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.linecorp.armeria.server.annotation.ExceptionHandlerFunction;
import com.practice.urlShortening.Response.ErrorResponse;
import com.practice.urlShortening.Response.UrlException.*;
import com.practice.urlShortening.Response.UrlResponse.ResultCode;

public class UrlExceptionHandler implements ExceptionHandlerFunction{
	
	private final ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public HttpResponse handleException(ServiceRequestContext ctx, HttpRequest req, Throwable cause) {
		try {
			if(cause instanceof NotExistException) {
				final HttpStatus status = HttpStatus.NOT_FOUND; //404
				return HttpResponse.of(status, MediaType.JSON_UTF_8, mapper.writeValueAsBytes(
						new ErrorResponse(status.reasonPhrase(),
										  cause.getMessage(),
										  ResultCode.NOT_FOUND,
										  Instant.now().toString())));
			}
			else if(cause instanceof DuplicatedException) {
				final HttpStatus status = HttpStatus.BAD_REQUEST; //400
				return HttpResponse.of(status, MediaType.JSON_UTF_8, mapper.writeValueAsBytes(
						new ErrorResponse(status.reasonPhrase(),
										  cause.getMessage(),
										  ResultCode.DUPLICATED,
										  Instant.now().toString())));
			}
			else if(cause instanceof UnauthorizedException) {
				final HttpStatus status = HttpStatus.UNAUTHORIZED; //401
				return HttpResponse.of(status, MediaType.JSON_UTF_8, mapper.writeValueAsBytes(
						new ErrorResponse(status.reasonPhrase(),
										  cause.getMessage(),
										  ResultCode.STORAGE_FAIL,
										  Instant.now().toString())));
			}
			else {
				final HttpStatus status = HttpStatus.UNKNOWN;
				return HttpResponse.of(status, MediaType.JSON_UTF_8, mapper.writeValueAsBytes(
						new ErrorResponse(status.reasonPhrase(),
										  cause.getMessage(),
										  ResultCode.OTHER_ERRORS,
										  Instant.now().toString())));
			}
		}catch(JsonProcessingException e) {
			return HttpResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, MediaType.JSON_UTF_8, "{\"error\":\"internal error\"}");
		}
	}	
}


